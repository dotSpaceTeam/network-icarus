package dev.dotspace.network.library.spigot.scoreboard;

import dev.dotspace.network.library.game.scoreboard.GameSidebarProvider;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

/**
 * Handle {@link ISidebar}. [GET, CREATE, REMOVE]
 */
public interface ISidebarProvider extends GameSidebarProvider<Player, Component> {
}
