package dev.dotspace.network.library.game.bridge;

import com.google.inject.Singleton;
import dev.dotspace.common.response.Response;
import dev.dotspace.network.client.RestClient;
import dev.dotspace.network.library.Library;
import dev.dotspace.network.library.exception.LibraryException;
import dev.dotspace.network.library.profile.ProfileType;
import dev.dotspace.network.library.profile.session.ISession;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


@Singleton
@Log4j2
public final class ClientMask implements IClientMask {
  private final Map<String, ISession> localSessions;

  public ClientMask() {
    this.localSessions = new HashMap<>();
  }


  @Override
  public @NotNull Response<Boolean> connect(@Nullable ProfileType profileType,
                                            @Nullable String uniqueId,
                                            @Nullable String name,
                                            @Nullable String address,
                                            @Nullable String texture,
                                            @Nullable String signature) {
    return Library.responseService().response(() -> {
      //Create client if present.
      RestClient.client()
          .profileRequest()
          .updateProfile(uniqueId, name, profileType)
          .getOptional()
          .orElseThrow(() -> new LibraryException("Error while creating profile."));

      log.info("Updated client={}.", uniqueId);


      RestClient.client()
          .profileRequest()
          .setAttribute(uniqueId, "skin", texture)
          .getOptional()
          .orElseThrow(() -> new LibraryException("Error while updating skin for "+uniqueId+"."));


      log.info("Update client={} -> texture={}.", uniqueId, texture);

      final ISession session = RestClient.client()
          .profileRequest()
          .createSession(uniqueId, address)
          .getOptional()
          //If session is null.
          .orElseThrow(() -> new LibraryException("Error while creating session for "+uniqueId+"."));

      log.info("Created new session for client={} -> session={}.", uniqueId, session.sessionId());

      //Locale session cache.
      this.localSessions.put(uniqueId, session);

      return true;
    });
  }

  @Override
  public @NotNull Response<Boolean> disconnect(@Nullable String uniqueId) {
    return Library.responseService().response(() -> {
      @Nullable final ISession session = this.localSessions.remove(uniqueId);
      //Check if session is present.
      if (session != null) {
        if (RestClient
            .client()
            .profileRequest()
            .completeSession(uniqueId, session.sessionId())
            .get() == null) {
          //Error while closing session.
          log.warn("Error while closing session={} for client={}.", session.sessionId(), uniqueId);
        } else {
          //Session closed successfully.
          log.info("Closed session={} for client={}.", session.sessionId(), uniqueId);
        }
      }

      return true;
    });
  }
}
