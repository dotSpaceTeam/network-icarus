package dev.dotspace.network.library.spigot.plugin;

import dev.dotspace.network.library.game.plugin.GamePlugin;
import dev.dotspace.network.library.game.plugin.GameDisableConfiguration;
import dev.dotspace.network.library.game.plugin.GameEnableConfiguration;
import dev.dotspace.network.library.game.plugin.GameLoadConfiguration;
import org.jetbrains.annotations.NotNull;

public interface IPlugin extends GamePlugin {
    /**
     * Run on load
     */
    void load(@NotNull final GameLoadConfiguration configuration);

    /**
     * Run on enable
     */
    void enable(@NotNull final GameEnableConfiguration configuration);

    /**
     * Run on disable
     */
    void disable(@NotNull final GameDisableConfiguration configuration);
}
