package dev.dotspace.network.library.spigot.plugin;


import cloud.commandframework.CommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import dev.dotspace.network.library.game.plugin.GamePlugin;
import dev.dotspace.network.library.game.plugin.PluginState;
import dev.dotspace.network.library.provider.Provider;
import dev.dotspace.network.library.spigot.event.AbstractListener;
import lombok.extern.log4j.Log4j2;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.util.Optional;
import java.util.stream.Stream;

@Log4j2
public abstract class AbstractPlugin extends JavaPlugin implements GamePlugin {
    /**
     * Plugin to handle system, cant be extended because {@link JavaPlugin} needed by default.
     */
    private final @NotNull SpigotPlugin spigotPlugin = new SpigotPlugin();
    /**
     * Get time plugin had been loaded
     */
    private long loadTime = 0;

    /**
     * Pass {@link SpigotPlugin#injector()}
     */
    @Override
    public @NotNull Injector injector() {
        return this.spigotPlugin.injector();
    }

    /**
     * Pass {@link SpigotPlugin#provider(Class)}
     */
    @Override
    public @NotNull <PROVIDER extends Provider> Optional<PROVIDER> provider(@Nullable Class<PROVIDER> providerClass) {
        return this.spigotPlugin.provider(providerClass);
    }

    /**
     * Pass {@link SpigotPlugin#handle(PluginState, Runnable)}
     */
    @Override
    public @NotNull GamePlugin handle(@Nullable PluginState state,
                                      @Nullable Runnable runnable) {
        return this.spigotPlugin.handle(state, runnable);
    }

    /**
     * Pass {@link SpigotPlugin#module(AbstractModule)}
     */
    @Override
    public @NotNull <MODULE extends AbstractModule> GamePlugin module(@Nullable MODULE module) {
        return this.spigotPlugin.module(module);
    }

    @Override
    public final void onLoad() {
        //Configure.
        this.module(new PluginModule(this));
        this.configure();
        //Set load timestamp
        this.loadTime = System.currentTimeMillis();

        this.spigotPlugin.executeRunnable(PluginState.PRE_LOAD);
        //--- Code start ---

        //--- Code end ---
        this.spigotPlugin.executeRunnable(PluginState.POST_LOAD);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void onEnable() {
        //Lock and finalize system.
        this.spigotPlugin.lock();

        this.spigotPlugin.executeRunnable(PluginState.PRE_ENABLE);
        //--- Code start ---

        //Reflections of this plugin.
        final Reflections reflections = new Reflections(this.getClass().getPackageName());

        log.info("Searching for listener. (Variables must be part of the local initializer.)");
        //Search for all Abstract listeners.
        this.reflectionAndConstruct(reflections, AbstractListener.class)
                //Register listeners.
                .forEach(abstractListener -> {
                    log.info("Constructed instance of listener={}.", abstractListener.getClass().getSimpleName());
                    this.getServer().getPluginManager().registerEvents(abstractListener, this);
                });

        log.info("Searching for commands. (Variables must be part of the local initializer.)");
        //Search fo commands
        this.reflectionAndConstruct(reflections, AbstractCommand.class)
                .forEach(abstractCommand -> {
                    log.info("Constructed instance of command={},", abstractCommand.getClass().getSimpleName());
                    try {
                        abstractCommand.configure(this.injector().getInstance(CommandManager.class));
                    } catch (final Exception exception) {
                        log.warn("Error while registering command={}.", abstractCommand.getClass().getSimpleName());
                    }
                });

        //--- Code end ---
        this.spigotPlugin.executeRunnable(PluginState.POST_ENABLE);

        //Info
        log.info("Plugin {} loaded and enabled totally. Took a total of {}ms",
                this.getName(),
                (System.currentTimeMillis() - this.loadTime));
    }

    /**
     * Get all class objects of type in path.
     *
     * @param reflections library to search.
     * @param typeClass   to search for.
     * @param <TYPE>      generic type of objects and class.
     * @return stream of all created instances.
     */
    private <TYPE> @NotNull Stream<TYPE> reflectionAndConstruct(@NotNull final Reflections reflections,
                                                                @NotNull final Class<TYPE> typeClass) {
        return reflections
                //Get all class
                .getSubTypesOf(typeClass)
                .stream()
                //Create instance from injector.
                .map(this.injector()::getInstance);
    }

    @Override
    public final void onDisable() {
        this.spigotPlugin.executeRunnable(PluginState.PRE_DISABLE);
        //--- Code start ---

        //--- Code end ---
        this.spigotPlugin.executeRunnable(PluginState.POST_DISABLE);
    }


}
