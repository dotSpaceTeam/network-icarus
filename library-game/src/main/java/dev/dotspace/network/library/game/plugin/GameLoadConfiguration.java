package dev.dotspace.network.library.game.plugin;

import com.google.inject.Module;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface GameLoadConfiguration extends GamePluginConfiguration {
    /**
     * Add module to plugin injector.
     *
     * @param module to add.
     * @return instance of configuration.
     */
    @NotNull GameLoadConfiguration addModule(@Nullable final Module module);

    /**
     * Remove module from plugin injector.
     *
     * @param module to remove.
     * @return instance of configuration.
     */
    @NotNull GameLoadConfiguration removeModule(@Nullable final Module module);

    /**
     * List all modules.
     *
     * @return copy of list with all modules.
     */
    @NotNull List<Module> modules();
}
