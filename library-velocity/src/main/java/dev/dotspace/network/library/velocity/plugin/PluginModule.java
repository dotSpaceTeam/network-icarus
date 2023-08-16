package dev.dotspace.network.library.velocity.plugin;

import com.velocitypowered.api.plugin.PluginManager;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.dotspace.network.library.game.plugin.AbstractPluginModule;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

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

        //Configure end.
        log.info("Configuration of PluginModule for {} done.", this.plugin().name());
    }
}
