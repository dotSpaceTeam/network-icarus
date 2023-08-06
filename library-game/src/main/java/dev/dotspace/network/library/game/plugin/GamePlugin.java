package dev.dotspace.network.library.game.plugin;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import dev.dotspace.network.library.provider.Provider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface GamePlugin {
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
    @NotNull GamePlugin handle(@Nullable final PluginState state,
                               @Nullable final Runnable runnable);

    /**
     * Register no module for injected. Execute on configure.
     *
     * @param module   to add.
     * @param <MODULE> generic type of module.
     * @return class instance.
     * @throws NullPointerException if module is null.
     * @throws IllegalStateException if plugin already configured.
     */
    @NotNull <MODULE extends AbstractModule> GamePlugin module(@Nullable final MODULE module);
}