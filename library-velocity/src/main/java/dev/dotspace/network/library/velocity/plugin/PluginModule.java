package dev.dotspace.network.library.velocity.plugin;

import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.velocity.VelocityCommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.dotspace.network.library.game.plugin.AbstractPluginModule;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;


@Log4j2
public final class PluginModule extends AbstractPluginModule<AbstractPlugin> {
  /**
   * See {@link AbstractPluginModule#AbstractPluginModule(Object)}
   */
  public PluginModule(@Nullable AbstractPlugin plugin) {
    super(plugin);
  }

  @Override
  protected void configure() {
    log.info("Configuring default PluginModule for {}.", this.plugin().name());
    //Configure start.

    this.bind(AbstractPlugin.class).toInstance(this.plugin());
    log.info("Defined plugin to '{}'.", this.plugin().name());

    //Get server instance.
    final ProxyServer proxyServer = this.plugin().server();

    this.bind(ProxyServer.class).toInstance(proxyServer);
    log.info("Defined server.");

    this.bind(PluginManager.class).toInstance(proxyServer.getPluginManager());
    log.info("Defined plugin manager.");

    log.info("Configuring cloud command framework...");
    try {
      final CommandManager<CommandSource> commandManager = new VelocityCommandManager<>(
          this.plugin().server().getPluginManager().fromInstance(this.plugin()).orElseThrow(),
          this.plugin().server(),
          CommandExecutionCoordinator.simpleCoordinator(),
          Function.identity(),
          Function.identity());

      this.bind(CommandManager.class).toInstance(commandManager);
      log.info("Defined command manger.");
    } catch (final Exception exception) {
      log.fatal("Error while initializing command manager (cloud). message={}", exception.getMessage());
    }

    //Configure end.
    log.info("Configuration of PluginModule for {} done.", this.plugin().name());
  }
}
