package dev.dotspace.network.library.spigot.plugin;

import dev.dotspace.network.library.game.plugin.AbstractGamePlugin;
import dev.dotspace.network.library.game.plugin.PluginState;
import org.jetbrains.annotations.Nullable;

/**
 * Class exists, because {@link org.bukkit.plugin.java.JavaPlugin} needs to be extended.
 * Hacky class.
 */
final class SpigotPlugin extends AbstractGamePlugin {
    /**
     * See {@link AbstractGamePlugin#executeRunnable(PluginState)}
     * There to be used in code.
     */
    @Override
    protected void executeRunnable(@Nullable PluginState state) {
        super.executeRunnable(state);
    }

    /**
     * Ignore.
     */
    @Override
    public void configure() {
        /*
         * Ignore
         */
    }

    /**
     * See {@link AbstractGamePlugin#lock()}
     * There to be used in code.
     */
    @Override
    protected void lock() {
        super.lock();
    }
}
