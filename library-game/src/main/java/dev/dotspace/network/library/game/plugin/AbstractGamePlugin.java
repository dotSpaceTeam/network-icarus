package dev.dotspace.network.library.game.plugin;

import cloud.commandframework.CommandManager;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import dev.dotspace.network.library.Library;
import dev.dotspace.network.library.game.command.AbstractCloudCommand;
import dev.dotspace.network.library.game.event.GameListener;
import dev.dotspace.network.library.provider.Provider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;


/**
 * Implementation for GamePlugin.
 */
@Log4j2
@Accessors(fluent=true)
public abstract class AbstractGamePlugin<LISTENER extends GameListener<?>, COMMAND extends AbstractCloudCommand> implements GamePlugin {
  /**
   * Injector of plugin -> {@link GamePlugin#injector()}
   */
  private @Nullable Injector injector;
  /**
   * Store values of state runnable.
   */
  private final @NotNull Multimap<PluginState, Runnable> stateRunnableMultimap;
  /**
   * State if system is done and configured
   */
  private boolean configured = false;

  private long loadTime = 0;

  /**
   * List modules of system.
   */
  private final @NotNull List<Module> moduleList;

  @Getter(AccessLevel.PROTECTED)
  @Setter(AccessLevel.PROTECTED)
  private @Nullable Class<?> entryClass;

  private @Nullable Class<LISTENER> listenerClass;
  private @Nullable Consumer<LISTENER> listenerConsumer;

  private @Nullable Class<COMMAND> commandClass;

  protected AbstractGamePlugin() {
    this.stateRunnableMultimap = HashMultimap.create();
    this.moduleList = new ArrayList<>();

    //Add library module.
    this.module(Library.module());
  }

  /**
   * Handle listener of plugin.
   *
   * @param listenerClass    of listener.
   * @param listenerConsumer consumer to handle listener.
   */
  protected void hookEventListener(@Nullable final Class<LISTENER> listenerClass,
                                   @Nullable final Consumer<LISTENER> listenerConsumer) {
    this.listenerClass = listenerClass;
    this.listenerConsumer = listenerConsumer;
  }


  protected void hookCommand(@Nullable final Class<COMMAND> commandClass) {
    this.commandClass = commandClass;
  }

  /**
   * See {@link GamePlugin#injector()}
   */
  @Override
  public final @NotNull Injector injector() {
    return Objects.requireNonNull(this.injector, "No injector defined, check plugin config.");
  }

  /**
   * See {@link GamePlugin#provider(Class)}
   */
  @Override
  public @NotNull <PROVIDER extends Provider> Optional<PROVIDER> provider(@Nullable Class<PROVIDER> providerClass) {
    return Optional
        .ofNullable(providerClass)
        //Map injector to provider.
        .map(this.injector()::getInstance);
  }

  @Override
  public final @NotNull GamePlugin handle(@Nullable PluginState state,
                                          @Nullable Runnable runnable) {
    //Null check
    Objects.requireNonNull(state);
    Objects.requireNonNull(runnable);

    //Add state and runnable
    this.stateRunnableMultimap.put(state, runnable);
    return this;
  }

  @Override
  public @NotNull <MODULE extends AbstractModule> GamePlugin module(@Nullable MODULE module) {
    //Null check
    Objects.requireNonNull(module);

    //Check state
    this.checkIfConfigured();

    this.moduleList.add(module);
    log.info("Added module={} to plugin injector.", module.getClass().getSimpleName());
    return this;
  }

  /**
   * Check if system is already configured.
   *
   * @throws IllegalStateException if already configured.
   */
  private void checkIfConfigured() {
    if (this.configured) {
      throw new IllegalStateException("Already configured. Method disabled.");
    }
  }

  /**
   * Set system as configured
   */
  protected void lock() {
    this.configured = true;
    log.info("System is now locked, no configuration can be done from now on.");
    this.injector = Guice.createInjector(this.moduleList);
    log.info("Created system injector.");
  }

  /**
   * Initialize plugin.
   */
  @SuppressWarnings("unchecked")
  protected synchronized void init(@Nullable final String name) {
    //Null checks
    Objects.requireNonNull(this.listenerClass);
    Objects.requireNonNull(this.listenerConsumer);
    Objects.requireNonNull(this.commandClass);

    //Set load timestamp
    this.loadTime = System.currentTimeMillis();

    log.info("Initializing plugin instance...");

    //Check if entry is present.
    if (this.entryClass == null) {
      log.info("No entry class defined using locale.");
      this.entryClass = this.getClass();
    }

    //Reflections of this plugin.
    final Reflections reflections = new Reflections(this.entryClass.getPackageName());

    //Register listener.
    log.info("Searching for listener. (Variables must be part of the local initializer.)");
    //Search for all Abstract listeners.
    this.createInstanceOf(reflections, this.listenerClass)
        //Register listeners.
        .forEach(abstractListener -> {
          log.info("Constructed instance of listener={}.", abstractListener.getClass().getSimpleName());
          this.listenerConsumer.accept(abstractListener);
        });
    //Registered listener.

    //Register commands.
    log.info("Searching for commands. (Variables must be part of the local initializer.)");
    final CommandManager<?> manager = this.injector().getInstance(CommandManager.class);
    //Search fo commands
    this.createInstanceOf(reflections, this.commandClass)
        //Register commands
        .forEach(abstractCommand -> {
          log.info("Constructed instance of command={},", abstractCommand.getClass().getSimpleName());
          try {
            abstractCommand.configure(manager);
          } catch (final Exception exception) {
            log.warn("Error while registering command={}.", abstractCommand.getClass().getSimpleName());
          }
        });
    //Registered commands.

    //Info
    log.info("Plugin {} loaded and enabled totally. Took a total of {}ms",
        name,
        (System.currentTimeMillis()-this.loadTime));
  }

  /**
   * Shutdown plugin.
   */
  protected synchronized void shutdown() {

  }


  /**
   * Execute runnable list for state.
   *
   * @param state to execute.
   * @throws NullPointerException if state is null.
   */
  protected void executeRunnable(@Nullable final PluginState state) {
    //Null check
    Objects.requireNonNull(state);

    this.stateRunnableMultimap
        //Get list of states.
        .get(state)
        //Execute them.
        .forEach(Runnable::run);
  }

  /**
   * Get all class objects of type in path.
   *
   * @param reflections library to search.
   * @param typeClass   to search for.
   * @param <TYPE>      generic type of objects and class.
   * @return stream of all created instances.
   */
  protected <TYPE> @NotNull Stream<TYPE> createInstanceOf(@NotNull final Reflections reflections,
                                                          @NotNull final Class<TYPE> typeClass) {
    return reflections
        //Get all class
        .getSubTypesOf(typeClass)
        .stream()
        //Create instance from injector. (All variables of class must be present in injector)
        .map(this.injector()::getInstance);
  }
}
