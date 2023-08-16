package dev.dotspace.network.library.spigot.plugin;

import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import com.google.inject.AbstractModule;
import dev.dotspace.network.library.game.plugin.AbstractPluginModule;
import lombok.extern.log4j.Log4j2;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Base {@link AbstractModule} for spigot.
 */
@Log4j2
public final class PluginModule extends AbstractPluginModule<JavaPlugin> {

    /**
     * See {@link AbstractPluginModule#AbstractPluginModule(Object)}
     */
    public PluginModule(@Nullable JavaPlugin javaPlugin) {
        super(javaPlugin);
    }

    /**
     * Configure module
     */
    @Override
    protected void configure() {
        log.info("Configuring default PluginModule for {}.", this.plugin().getName());
        //Configure start.
        this.bind(JavaPlugin.class).toInstance(this.plugin());
        log.info("Defined plugin to '{}'.", this.plugin().getName());

        //Get server instance.
        final Server server = this.plugin().getServer();

        this.bind(Server.class).toInstance(server);
        log.info("Defined server.");

        this.bind(PluginManager.class).toInstance(server.getPluginManager());
        log.info("Defined plugin manager.");

        this.bind(ServicesManager.class).toInstance(server.getServicesManager());
        //Defined services.

        log.info("Configuring cloud command framework...");
        try {
            final CommandManager<CommandSender> commandManager = new BukkitCommandManager<>(
                    this.plugin(),
                    CommandExecutionCoordinator.simpleCoordinator(),
                    Function.identity(),
                    Function.identity());

            this.bind(CommandManager.class).toInstance(commandManager);
            log.info("Defined command manger.");
        } catch (final Exception exception) {
            log.fatal("Error while initializing command manager (cloud). message={}", exception.getMessage());
        }

        //Configure end.
        log.info("Configuration of PluginModule for {} done.", this.plugin().getName());
    }
}
