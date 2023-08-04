package dev.dotspace.network.library.spigot.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

final class DefaultPluginModule extends AbstractSpigotModule {
    /**
     * See {@link AbstractSpigotModule#AbstractSpigotModule(JavaPlugin)}
     */
    DefaultPluginModule(@Nullable JavaPlugin plugin) {
        super(plugin);

        System.out.println("Loader: " + plugin().getClass().getPackageName());


    }
}
