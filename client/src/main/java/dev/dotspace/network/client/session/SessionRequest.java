package dev.dotspace.network.client.session;

import dev.dotspace.common.response.Response;
import dev.dotspace.network.client.web.AbstractRequest;
import dev.dotspace.network.library.profile.session.IPlaytime;
import dev.dotspace.network.library.profile.session.ISession;
import dev.dotspace.network.library.profile.session.ImmutablePlaytime;
import dev.dotspace.network.library.profile.session.ImmutableSession;
import dev.dotspace.network.library.session.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


/**
 * Manipulate session as client.
 */

public final class SessionRequest extends AbstractRequest implements ISessionRequest {
  /*
   * See {@link ISessionRequest#getSessionList(String)}.

   @Override public @NotNull Response<List<ISession>> getSessionList(@Nullable String uniqueId) {
   return this
   .responseService()
   .response(() -> {
   //Null check
   Objects.requireNonNull(uniqueId);

   //Send request
   return this.client()
   //Get all
   .get("/v1/session/"+uniqueId, SessionList.class)
   //Map
   .stream()
   .map(immutableSession -> (ISession) immutableSession)
   //Convert stream to list.
   .toList();
   });
   }
   */

  /**
   * See {@link ISessionRequest#getSession(String, Long)}.
   */
  @Override
  public @NotNull Response<ISession> getSession(@Nullable String uniqueId,
                                                @Nullable Long sessionId) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(sessionId);

      //Send request
      return this.client()
          .get("/v1/session/"+uniqueId+"/"+sessionId, ImmutableSession.class);
    });
  }

  /**
   * See {@link ISessionRequest#getPlaytime(String)}.
   */
  @Override
  public @NotNull Response<IPlaytime> getPlaytime(@Nullable String uniqueId) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);

      //Send request
      return this.client()
          .get("/v1/playtime/"+uniqueId, ImmutablePlaytime.class);
    });
  }

  /**
   * See {@link ISessionRequest#createSession(String)}.
   */
  @Override
  public @NotNull Response<ISession> createSession(@Nullable String uniqueId) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);

      //Send request
      return this.client()
          .post("/v1/session/"+uniqueId, ImmutableSession.class, null);
    });
  }

  /**
   * See {@link ISessionRequest#completeSession(String, Long)}.
   */
  @Override
  public @NotNull Response<ISession> completeSession(@Nullable String uniqueId,
                                                     @Nullable Long sessionId) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(sessionId);

      //Send request
      return this.client()
          .put("/v1/session/"+uniqueId+"/"+sessionId, ImmutableSession.class, null);
    });
  }
}