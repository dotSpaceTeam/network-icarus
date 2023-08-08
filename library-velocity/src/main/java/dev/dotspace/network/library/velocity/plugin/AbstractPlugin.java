package dev.dotspace.network.library.velocity.plugin;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.dotspace.network.library.game.plugin.AbstractGamePlugin;
import dev.dotspace.network.library.game.plugin.PluginState;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

@Log4j2
@Getter
@Accessors(fluent = true)
public abstract class AbstractPlugin extends AbstractGamePlugin {
    private final @NotNull ProxyServer server;
    private final @NotNull Logger logger;

    @Inject
    public AbstractPlugin(@NotNull final ProxyServer server,
                          @NotNull final Logger logger) {
        this.server = server;
        this.logger = logger;

        this.module(new PluginModule(this));
        this.configure();

        this.executeRunnable(PluginState.PRE_LOAD);
        //--- Code start ---

        //--- Code end ---
        this.executeRunnable(PluginState.POST_LOAD);
    }

    @Subscribe
    public void handleEnable(@NotNull final ProxyInitializeEvent event) {
        this.lock();

        this.executeRunnable(PluginState.PRE_ENABLE);
        //--- Code start ---

        //--- Code end ---
        this.executeRunnable(PluginState.POST_ENABLE);
    }

    @Subscribe
    public void handleShutdown(@NotNull final ProxyShutdownEvent event) {
        this.executeRunnable(PluginState.PRE_DISABLE);
        //--- Code start ---

        //--- Code end ---
        this.executeRunnable(PluginState.POST_DISABLE);
    }

    /**
     * Get name of plugin class.
     *
     * @return class name.
     */
    public @NotNull String name() {
        return this.getClass().getName();
    }
}
