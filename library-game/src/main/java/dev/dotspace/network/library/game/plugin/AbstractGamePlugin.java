package dev.dotspace.network.library.game.plugin;

import cloud.commandframework.CommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import dev.dotspace.common.function.ThrowableRunnable;
import dev.dotspace.network.client.RestClient;
import dev.dotspace.network.library.Library;
import dev.dotspace.network.library.common.StateMap;
import dev.dotspace.network.library.game.command.AbstractCloudCommand;
import dev.dotspace.network.library.game.event.GameListener;
import dev.dotspace.network.library.game.message.context.ContextType;
import dev.dotspace.network.library.game.message.context.IMessageContext;
import dev.dotspace.network.library.game.message.context.MessageContext;
import dev.dotspace.network.library.jvm.IResourceInfo;
import dev.dotspace.network.library.jvm.ImmutableResourceInfo;
import dev.dotspace.network.library.message.config.MessageConfig;
import dev.dotspace.network.library.provider.Provider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
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
public abstract class AbstractGamePlugin<PLAYER, LISTENER extends GameListener<?>,
    COMMAND extends AbstractCloudCommand> implements GamePlugin<PLAYER> {
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
    //Null check
    Objects.requireNonNull(this.injector, "No injector defined, check plugin config.");

    return this.injector;
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

  /**
   * See {@link GamePlugin#message(Object, ContextType, String)}
   */
  @Override
  public @NotNull IMessageContext message(@Nullable PLAYER player,
                                          @Nullable ContextType contextType,
                                          @Nullable String content) {
    return new MessageContext(contextType, content, this.playerLocale(player));
  }

  @Override
  public @NotNull IResourceInfo resourceInfo() {
    return ImmutableResourceInfo.now();
  }

  /**
   * Get locale of player.
   */
  protected abstract @NotNull Locale playerLocale(@Nullable final PLAYER player);

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

    //Reflections of this plugin.
    final Reflections reflections = new Reflections(this.entryClass.getPackageName());

    //Scan system for content.
    this.scanForListener(reflections);
    this.scanForCommands(reflections);

    //Configure client.
    Library.jvmParameter("node")
        .ifPresentOrElse(this::configureClient, () -> {
      //Warn -> no parameter present
      log.info("No parameter for node (-Dicarus-node) present.");
    });

    //Info
    log.info("Plugin {} loaded and enabled totally. Took a total of {}ms",
        name,
        (System.currentTimeMillis()-loadTime));
  }

  /**
   * Scan plugin package for listeners
   */
  private void scanForListener(@NotNull final Reflections reflections) {
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
  }

  /**
   * Scan plugin package for commands
   */
  private void scanForCommands(@NotNull final Reflections reflections) {
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
  }

  private void configureClient(@NotNull final String node) {
    log.info("Enabling client.");
    //Connect.
    RestClient.connect(node);
    log.info("Client connected pulling node information...");

  }

  /**
   * Load message from config.
   */
  private void initMessages() {
    log.info("Searching for local message...");
    try {
      Library.configService()
          .readResource(this.entryClass, MessageConfig.class, "message.json")
          .ifPresentOrElse(messageConfig -> {
            log.info("Messages present, sending requests.");

            //Loop trough every locale - key message pair.
            messageConfig.messageMap().forEach((locale, keyMessageMap) -> {
              //Loop trough every key message pair.
              keyMessageMap.forEach((key, message) -> {
                //Log
                log.info("Requesting persistent message={} for locale={}", locale.toLanguageTag(), key);

                //Send request
                RestClient.client().messageRequest().updateMessage(locale, key, message, true);
              });
            });

          }, () -> {
            log.info("No message.json present.");
          });
    } catch (final IOException exception) {
      exception.printStackTrace();
      log.warn("Error while loading message.json.");
    }
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
