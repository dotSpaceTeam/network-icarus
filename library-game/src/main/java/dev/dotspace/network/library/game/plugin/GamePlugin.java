package dev.dotspace.network.library.game.plugin;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import dev.dotspace.common.function.ThrowableRunnable;
import dev.dotspace.network.library.game.message.context.ContextType;
import dev.dotspace.network.library.game.message.context.IMessageContext;
import dev.dotspace.network.library.jvm.IResourceInfo;
import dev.dotspace.network.library.provider.Provider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


public interface GamePlugin<PLAYER> {
  /**
   * Configure plugin.
   */
  void configure();

  /**
   * Get plugin {@link Injector}.
   *
   * @return injector - Present after load.
   * @throws NullPointerException if {@link Injector} is null.
   */
  @NotNull Injector injector();

  /**
   * Get a provider of plugin.
   *
   * @param providerClass to get provider from.
   * @param <PROVIDER>    to get from class.
   * @return provider wrapped in optional -> null if no provider is present.
   */
  @NotNull <PROVIDER extends Provider> Optional<PROVIDER> provider(@Nullable final Class<PROVIDER> providerClass);


  /**
   * Run on state ->
   *
   * @throws NullPointerException if state or runnable is null.
   */
  @NotNull GamePlugin<PLAYER> handle(@Nullable final PluginState state,
                                     @Nullable final ThrowableRunnable runnable);

  /**
   * Register no module for injected. Execute on configure.
   *
   * @param module   to add.
   * @param <MODULE> generic type of module.
   * @return class instance.
   * @throws NullPointerException  if module is null.
   * @throws IllegalStateException if plugin already configured.
   */
  @NotNull <MODULE extends AbstractModule> GamePlugin<PLAYER> module(@Nullable final MODULE module);

  /**
   * Get context for player.
   *
   * @param player      to get context for.
   * @param contextType type of context to build message.
   * @param content     message or key.
   * @return created context.
   */
  @NotNull IMessageContext message(@Nullable final PLAYER player,
                                   @Nullable final ContextType contextType,
                                   @Nullable final String content);

  /**
   * Get current resources.
   *
   * @return instance of current system.
   */
  @NotNull IResourceInfo resourceInfo();
}
