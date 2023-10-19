package dev.dotspace.network.library.velocity.self.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.proxy.Player;
import dev.dotspace.network.library.velocity.event.AbstractListener;
import dev.dotspace.network.library.velocity.self.session.SessionMap;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


/**
 * Class to handle player disconnect.
 */
public final class DisconnectHandle extends AbstractListener {
  /**
   * Handle sessions with this singleton map.
   */
  @Inject
  private SessionMap sessionMap;

  /**
   * Handle disconnect
   */
  @Subscribe(order=PostOrder.FIRST)
  public void handle(@NotNull final DisconnectEvent event) {
    //Player who disconnected.
    final Player player = event.getPlayer();
    //Get player uniqueId
    final UUID uniqueId = player.getUniqueId();


  }

  private void closeSession() {

  }

}
