package dev.dotspace.network.library.game.plugin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import dev.dotspace.network.library.provider.Provider;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Accessors(fluent = true)
public abstract class AbstractGamePlugin implements GamePlugin {
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
    /**
     * List modules of system.
     */
    private final @NotNull List<Module> moduleList;

    protected AbstractGamePlugin() {
        this.stateRunnableMultimap = HashMultimap.create();
        this.moduleList = new ArrayList<>();
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
}
