package dev.dotspace.network.library.game.bridge;

import dev.dotspace.common.response.Response;
import dev.dotspace.network.client.Client;
import dev.dotspace.network.library.Library;
import dev.dotspace.network.library.exception.LibraryException;
import dev.dotspace.network.library.profile.ProfileType;
import dev.dotspace.network.library.session.ISession;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


@Log4j2
public class AbstractDataBridge implements IDataBridge {

  private final Map<String, ISession> localSessions;

  public AbstractDataBridge() {
    this.localSessions = new HashMap<>();
  }


  @Override
  public @NotNull Response<Boolean> connect(@Nullable ProfileType profileType,
                                            @Nullable String uniqueId,
                                            @Nullable String name,
                                            @Nullable String texture) {
    return Library.responseService().response(() -> {
      //Create client if present.
      if (Client.client()
          .profileRequest()
          .createProfile(uniqueId, profileType)
          .get() == null) {
        throw new LibraryException("Error while creating profile.");
      }

      log.info("Updated client={}.", uniqueId);

      if (Client.client()
          .profileRequest()
          .setAttribute(uniqueId, "name", name)
          .get() == null) {
        throw new LibraryException("Error while updating name of "+uniqueId+".");
      }

      log.info("Updated client={} -> name={}.", uniqueId, name);

      if (Client.client()
          .profileRequest()
          .setAttribute(uniqueId, "skin", texture)
          .get() == null) {
        throw new LibraryException("Error while updating skin for "+uniqueId+".");
      }

      log.info("Update client={} -> texture={}.", uniqueId, texture);

      @Nullable final ISession session = Client.client()
          .sessionRequest()
          .createSession(uniqueId)
          .get();

      //If session is null.
      if (session == null) {
        throw new LibraryException("Error while creating session for "+uniqueId+".");
      }

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
        if (Client
            .client()
            .sessionRequest()
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
