package dev.dotspace.network.library.velocity.self.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.ResultedEvent.ComponentResult;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.util.GameProfile;
import dev.dotspace.network.client.Client;
import dev.dotspace.network.library.profile.ProfileType;
import dev.dotspace.network.library.profile.session.ISession;
import dev.dotspace.network.library.velocity.event.AbstractListener;
import dev.dotspace.network.library.velocity.profile.ISkin;
import dev.dotspace.network.library.velocity.profile.Skin;
import dev.dotspace.network.library.velocity.self.session.SessionMap;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;


/**
 * Class to handle player login.
 */
@Log4j2
public final class LoginHandle extends AbstractListener {
  /**
   * Handle sessions with this singleton map.
   */
  @Inject
  private SessionMap sessionMap;

  /**
   * Handle login
   */
  @Subscribe(order=PostOrder.FIRST)
  public void handle(@NotNull final LoginEvent event) {
    //Player who disconnected.
    final Player player = event.getPlayer();
    //Get player uniqueId
    final UUID uniqueId = player.getUniqueId();
    //Get player address.
    final String address = player.getRemoteAddress().getHostString();
    //Get version of player.
    final int protocolVersion = player.getProtocolVersion().getProtocol();

    //Clear Player Title -> If player has other title from server, this will remove it
    player.clearTitle();

    //Update profile.
    if (!this.updateProfile(uniqueId.toString(), player.getUsername(), ProfileType.JAVA)) {
      //Todo on error kick.
      event.setResult(ComponentResult.denied(Component.text("Error profile.")));
      return;
    }
    //Log update
    log.info("Updated client={}.", uniqueId);

    //Update skin
    if (!this.updateSkin(uniqueId.toString(), player.getGameProfile())) {
      //Todo on error kick.
      event.setResult(ComponentResult.denied(Component.text("Error skin.")));
      return;
    }
    //Log update
    log.info("Updated textures for client={}.", uniqueId);

    //Open session for player.
    if (!this.openSession(uniqueId.toString(), address)) {
      //Todo on error kick.
      event.setResult(ComponentResult.denied(Component.text("Error session.")));
      return;
    }

    //Todo: Check if client is banned
  }

  /**
   * Update player profile -> returns true if success.
   */
  private boolean updateProfile(@NotNull final String uniqueId,
                                @NotNull final String username,
                                @NotNull final ProfileType profileType) {
    //Create client if present.
    try {
      Client.client()
          .profileRequest()
          //Update with player values.
          .updateProfile(uniqueId, username, profileType)
          .block();
      //Error while updating profile.
    } catch (final Throwable throwable) {
      return false;
    }
    return true;
  }

  /**
   * Update player skin -> return true if updated or empty (without error.)
   */
  private boolean updateSkin(@NotNull final String uniqueId,
                             @NotNull final GameProfile gameProfile) {
    //Get skin of profile
    @Nullable final ISkin skin = Skin.fromProfile(gameProfile).orElse(null);

    //Skin is empty return true.
    if (skin == null) {
      return true;
    }

    try {
      Client.client()
          .profileRequest()
          //Update skin.
          .setAttribute(uniqueId, "skin", skin.value())
          .block();
      //Error while updating skin.
    } catch (final Throwable throwable) {
      return false;
    }
    return true;
  }

  /**
   * Open session for player -> return true if opened.
   */
  private boolean openSession(@NotNull final String uniqueId,
                              @NotNull final String address) {
    try {
      final ISession session = Client.client()
          .profileRequest()
          .createSession(uniqueId, address)
          .block();
      //Error while creating sessions skin.

      //Pass to map
      this.sessionMap.add(uniqueId, session);
    } catch (final Throwable throwable) {
      return false;
    }
    return true;
  }
}
