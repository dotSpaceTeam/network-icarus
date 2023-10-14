package dev.dotspace.network.library.velocity.self.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import dev.dotspace.network.library.velocity.event.AbstractListener;
import dev.dotspace.network.library.velocity.profile.Skin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


/**
 * Class to handle player login.
 */
public final class LoginHandle extends AbstractListener {
  /**
   * Handle login
   */
  @Subscribe(order=PostOrder.FIRST)
  public void handle(@NotNull final LoginEvent event) {
    //Player who disconnected.
    final Player player = event.getPlayer();
    //Get player uniqueIds
    final UUID uniqueId = player.getUniqueId();
    //Get player address.
    final String address = player.getRemoteAddress().getHostString();
    //Get version of player.
    final int protocolVersion = player.getProtocolVersion().getProtocol();

    //Update texture -> parse skin from profile.
    Skin.fromProfile(player.getGameProfile())
        //Use skin
        .ifPresent(skin -> {

        });

    //Todo: Check if client is banned

    //Clear Player Title -> If player has other title from server, this will remove it
    player.clearTitle();
  }


}
