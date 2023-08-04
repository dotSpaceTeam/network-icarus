package dev.dotspace.network.library.spigot.event;

import com.google.inject.Inject;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractListener implements Listener {
    /**
     * Get trough {@link AbstractListener#plugin()}
     */
    @Inject
    private JavaPlugin plugin;

    /**
     * Get plugin stored in variable.
     */
    public @NotNull JavaPlugin plugin() {
        return this.plugin;
    }
}
