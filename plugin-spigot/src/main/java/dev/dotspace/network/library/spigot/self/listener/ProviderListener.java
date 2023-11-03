package dev.dotspace.network.library.spigot.self.listener;

import com.google.inject.Inject;
import dev.dotspace.network.client.RestClient;
import dev.dotspace.network.library.game.bridge.IClientMask;
import dev.dotspace.network.library.game.mojang.MojangUtils;
import dev.dotspace.network.library.profile.ProfileType;
import dev.dotspace.network.library.spigot.event.AbstractListener;
import dev.dotspace.network.library.spigot.scoreboard.ISidebarProvider;
import dev.dotspace.network.library.spigot.self.IcarusPlugin;
import dev.dotspace.network.library.spigot.self.message.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;


/**
 * Handle providers of library.
 */
public final class ProviderListener extends AbstractListener {
  @Inject
  private ISidebarProvider sidebarProvider;

  @Inject
  private IClientMask clientMask;

  /**
   * Handle if player login to server.
   * LOWEST priority means this event will be triggered as first.
   */
  @EventHandler(priority=EventPriority.LOWEST)
  public void handle(@NotNull final AsyncPlayerPreLoginEvent event) {
    //Check if connection is valid
    if (RestClient.disconnected()) {
      //Kick because client is deactivated.
      event.disallow(Result.KICK_OTHER, Message.CLIENT_OFFLINE_KICK);
      return;
    }

    //Pass connect
    this.handleConnect(event);
  }

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

    //Pass disconnect.
    this.handleDisconnect(event);
  }

  private void handleConnect(@NotNull final AsyncPlayerPreLoginEvent event) {
    //Check if proxy is present.
    if (presentProxy()) {
      return;
    }

    //Return if client is absent.
    if (RestClient.disconnected()) {
      return;
    }

    final ProfileType profileType = ProfileType.JAVA;
    final String uniqueId = event.getUniqueId().toString();
    final String name = event.getName();
    final String address = event.getAddress().getHostAddress();

    event.getPlayerProfile().getProperties().forEach(profileProperty -> {
      System.out.println("Texture "+
          MojangUtils.texturesIdMapFromBase64(profileProperty.getValue()));
      System.out.println("Signature "+profileProperty.getSignature());
    });

    try {
      if (!this.clientMask.connect(profileType, uniqueId, name, address, "", "")
          .getOptional()
          .orElse(false)) {
        event.disallow(Result.KICK_OTHER, Message.CLIENT_OFFLINE_KICK);
        return;
      }
    } catch (final InterruptedException exception) {
    }
  }

  private void handleDisconnect(@NotNull final PlayerQuitEvent event) {
//Check if proxy is present.
    if (presentProxy()) {
      return;
    }

    //Return if client is absent.
    if (RestClient.disconnected()) {
      return;
    }

    final String uniqueId = event.getPlayer().getUniqueId().toString();

    try {
      if (!this.clientMask.disconnect(uniqueId)
          .getOptional()
          .orElse(false)) {
        return;
      }
    } catch (final InterruptedException exception) {
    }
  }

  private boolean presentProxy() {
    return ((IcarusPlugin) this.plugin()).proxy();
  }
}
