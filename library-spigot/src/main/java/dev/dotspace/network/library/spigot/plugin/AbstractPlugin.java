package dev.dotspace.network.library.spigot.plugin;


import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.dotspace.network.library.game.plugin.GameDisableConfiguration;
import dev.dotspace.network.library.game.plugin.GameEnableConfiguration;
import dev.dotspace.network.library.game.plugin.LoadConfiguration;
import dev.dotspace.network.library.provider.Provider;
import dev.dotspace.network.library.spigot.event.AbstractListener;
import lombok.extern.log4j.Log4j2;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;
import java.util.Optional;


@SpringBootApplication
@Log4j2
public abstract class AbstractPlugin extends JavaPlugin implements IPlugin {
    /**
     * Injector of plugin -> {@link IPlugin#injector()}
     */
    private @Nullable Injector injector;

    @Override
    public final void onLoad() {
        final LoadConfiguration configuration = new LoadConfiguration();

        //Pass load
        this.load(configuration);

        //Add default module.
        configuration.addModule(new DefaultPluginModule(this));

        //Create instances
        this.injector = Guice.createInjector(configuration.modules());
    }

    @Override
    public final void onEnable() {
        //Reflections of this plugin.
        final Reflections reflections = new Reflections(this.getClass().getPackageName());

        log.info("Searching for listener. (Instances for AbstractListener and empty constructors.)");
        reflections
                .getSubTypesOf(AbstractListener.class)
                .stream()
                //Create instance from injector.
                .map(this.injector()::getInstance)
                //Register listeners.
                .forEach(abstractListener -> {
                    log.info("Constructed instance of listener={}", abstractListener.getClass().getSimpleName());
                    this.getServer().getPluginManager().registerEvents(abstractListener, this);
                });


        //Pass enable
        this.enable(new GameEnableConfiguration() {
        });
    }

    @Override
    public final void onDisable() {
        //Pass disable
        this.disable(new GameDisableConfiguration() {
        });
    }

    @Override
    public @NotNull Injector injector() {
        return Objects.requireNonNull(this.injector, "No injector defined, check plugin config.");
    }

    /**
     * See {@link IPlugin#provider(Class)}
     */
    @Override
    public @NotNull <PROVIDER extends Provider> Optional<PROVIDER> provider(@Nullable Class<PROVIDER> providerClass) {
        return Optional
                .ofNullable(providerClass)
                //Map injector to provider.
                .map(this.injector()::getInstance);
    }
}
