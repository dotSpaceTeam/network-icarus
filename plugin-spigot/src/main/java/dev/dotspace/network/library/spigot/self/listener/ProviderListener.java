package dev.dotspace.network.library.spigot.self.listener;

import com.google.inject.Inject;
import dev.dotspace.network.library.spigot.event.AbstractListener;
import dev.dotspace.network.library.spigot.scoreboard.ISidebarProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;


/**
 * Handle providers of library.
 */
public final class ProviderListener extends AbstractListener {

  @Inject
  private ISidebarProvider sidebarProvider;

  /**
   * Handle if player joins server.
   * LOWEST priority means this event will be triggered as first.
   */
  @EventHandler(priority=EventPriority.LOWEST)
  public void handle(@NotNull final PlayerJoinEvent event) {
    final Player player = event.getPlayer();

    //Set default join message to null.
    event.joinMessage(null);
  }

  /**
   * Handle if player quit server.
   * HIGHEST priority means this event will be triggered as last.
   */
  @EventHandler(priority=EventPriority.HIGHEST)
  public void handle(@NotNull final PlayerQuitEvent event) {
    final Player player = event.getPlayer();

    //Set default quit message to null.
    event.quitMessage(null);

    //Remove from sidebar.
    this.sidebarProvider.remove(player);
  }
}
