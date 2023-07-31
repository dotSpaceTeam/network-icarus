package dev.dotspace.network.library.spigot.scoreboard;

import dev.dotspace.network.library.game.scoreboard.ISidebarProvider;
import dev.dotspace.network.library.provider.Provider;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Handle {@link ISpigotSidebar}. [GET, CREATE, REMOVE]
 */
public interface ISpigotSidebarProvider extends ISidebarProvider<Player, Component> {
}
