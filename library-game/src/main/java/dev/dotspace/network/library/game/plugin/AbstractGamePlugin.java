package dev.dotspace.network.library.game.plugin;

import cloud.commandframework.CommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import dev.dotspace.common.function.ThrowableRunnable;
import dev.dotspace.network.client.Client;
import dev.dotspace.network.library.Library;
import dev.dotspace.network.library.common.StateMap;
import dev.dotspace.network.library.game.command.AbstractCloudCommand;
import dev.dotspace.network.library.game.event.GameListener;
import dev.dotspace.network.library.game.message.GameMessageComponent;
import dev.dotspace.network.library.game.plugin.config.PluginConfig;
import dev.dotspace.network.library.message.IMessage;
import dev.dotspace.network.library.message.IMessageComponent;
import dev.dotspace.network.library.message.content.IPersistentMessage;
import dev.dotspace.network.library.provider.Provider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;


/**
 * Implementation for GamePlugin.
 */
@Log4j2
@SuppressWarnings("all")
@Accessors(fluent=true)
public abstract class AbstractGamePlugin<LISTENER extends GameListener<?>, COMMAND extends AbstractCloudCommand> implements GamePlugin {
  /**
   * Injector of plugin -> {@link GamePlugin#injector()}
   */
  private @Nullable Injector injector;
  /**
   * Store values of state runnable.
   */
  private final @NotNull StateMap<PluginState> stateMap;
  /**
   * State if system is done and configured
   */
  private boolean configured = false;

  /**
   * List modules of system.
   */
  private final @NotNull List<Module> moduleList;
  /**
   * Configuartion of plugin.
   */
  private @Nullable PluginConfig pluginConfig;

  @Getter(AccessLevel.PROTECTED)
  @Setter(AccessLevel.PROTECTED)
  private @Nullable Class<?> entryClass;

  private @Nullable Class<LISTENER> listenerClass;
  private @Nullable Consumer<LISTENER> listenerConsumer;

  private @Nullable Class<COMMAND> commandClass;

  protected AbstractGamePlugin() {
    this.stateMap = StateMap.createMap();
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
                                          @Nullable ThrowableRunnable runnable) {
    //Null check
    Objects.requireNonNull(state);
    Objects.requireNonNull(runnable);

    //Add state and runnable
    this.stateMap.append(state, runnable);
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

  @Override
  public @NotNull IMessageComponent<Component> message(@Nullable String message) {
    //Null check
    Objects.requireNonNull(message);

    return new GameMessageComponent(() -> message);
  }

  @Override
  public @NotNull IMessageComponent<Component> persistentMessage(@Nullable Locale locale,
                                                                 @Nullable String key) {
    //Null check
    Objects.requireNonNull(locale);
    Objects.requireNonNull(key);

    return new GameMessageComponent(() -> {
      return Client.client()
          .messageRequest()
          //Request message from master.
          .getMessage(locale, key)
          .getOptional()
          //Get stored message
          .map(IMessage::message)
          //Else set error.
          .orElse("Error while requesting message.");
    });
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
    long loadTime = System.currentTimeMillis();

    log.info("Initializing plugin instance...");

    //Check if entry is present.
    if (this.entryClass == null) {
      log.info("No entry class defined using locale.");
      this.entryClass = this.getClass();
    }

    //Load config
    try {
      this.pluginConfig = Library.configService()
          .readResource(this.entryClass, PluginConfig.class, "icarus.json")
          .orElseGet(() -> {
            log.info("No config present, using default one.");

            //Default config
            return PluginConfig.defaultConfig();
          });
    } catch (final IOException exception) {
      log.warn("Error while loading icarus.json.");
      throw new RuntimeException(exception);
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

    //Client
    if (this.pluginConfig.clientAutoConnect()) {
      log.info("Enabling client.");
      //Connect.
      Client.enable();
    }

    //Info
    log.info("Plugin {} loaded and enabled totally. Took a total of {}ms",
        name,
        (System.currentTimeMillis()-loadTime));
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
    this.stateMap.executeRunnable(state);
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
