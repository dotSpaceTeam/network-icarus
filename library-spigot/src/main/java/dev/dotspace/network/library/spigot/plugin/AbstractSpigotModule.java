package dev.dotspace.network.library.spigot.plugin;

import com.google.inject.AbstractModule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Base {@link AbstractModule} for spigot.
 */
@Log4j2
@Accessors(fluent = true)
public abstract class AbstractSpigotModule extends AbstractModule {
    /**
     * Plugin of module.
     */
    @Getter(AccessLevel.PACKAGE)
    private final @NotNull JavaPlugin plugin;

    /**
     * Construct.
     *
     * @param plugin to set as {@link AbstractSpigotModule#plugin}
     * @throws NullPointerException if plugin is null.
     */
    protected AbstractSpigotModule(@Nullable final JavaPlugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
    }

    @Override
    protected void configure() {
        log.info("Configuring default AbstractSpigotModule...");
        //Configure start.
        this.bind(JavaPlugin.class).toInstance(this.plugin);
        log.info("Defined plugin to '{}'.", this.plugin.getName());

        this.bind(Server.class).toInstance(this.plugin.getServer());
        log.info("Defined server.");

        this.bind(PluginManager.class).toInstance(this.plugin.getServer().getPluginManager());
        log.info("Defined plugin manager.");

        this.bind(ServicesManager.class).toInstance(this.plugin.getServer().getServicesManager());
        //Defined services.

        //Configure end.
        log.info("Configuration of AbstractSpigotModule done.");
        //Others will come here.
    }
}
