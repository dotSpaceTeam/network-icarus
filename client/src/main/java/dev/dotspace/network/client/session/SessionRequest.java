package dev.dotspace.network.client.session;

import dev.dotspace.common.SpaceLibrary;
import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.network.client.web.AbstractRequest;
import dev.dotspace.network.library.session.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Manipulate session as client.
 */
@Component
public final class SessionRequest extends AbstractRequest implements ISessionManipulator {
  /**
   * See {@link ISessionManipulator#getSessionList(String)}.
   */
  @Override
  public @NotNull CompletableResponse<List<ISession>> getSessionList(@Nullable String profileId) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(profileId);

      //Send request
      return this.client()
        //Get all
        .get("/v1/session/" + profileId, SessionList.class)
        //Map
        .stream()
        .map(immutableSession -> (ISession) immutableSession)
        //Convert stream to list.
        .toList();
    });
  }

  /**
   * See {@link ISessionManipulator#getSession(String, Long)}.
   */
  @Override
  public @NotNull CompletableResponse<ISession> getSession(@Nullable String profileId,
                                                           @Nullable Long sessionId) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(profileId);
      Objects.requireNonNull(sessionId);

      //Send request
      return this.client()
        .get("/v1/session/" + profileId + "/" + sessionId, ImmutableSession.class);
    });
  }

  /**
   * See {@link ISessionManipulator#getPlaytime(String)}.
   */
  @Override
  public @NotNull CompletableResponse<IPlaytime> getPlaytime(@Nullable String profileId) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(profileId);

      //Send request
      return this.client()
        .get("/v1/playtime/" + profileId, ImmutablePlaytime.class);
    });
  }

  /**
   * See {@link ISessionManipulator#createSession(String)}.
   */
  @Override
  public @NotNull CompletableResponse<ISession> createSession(@Nullable String profileId) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(profileId);

      //Send request
      return this.client()
        .post("/v1/session/" + profileId, ImmutableSession.class, null);
    });
  }

  /**
   * See {@link ISessionManipulator#completeSession(String, Long)}.
   */
  @Override
  public @NotNull CompletableResponse<ISession> completeSession(@Nullable String profileId,
                                                                @Nullable Long sessionId) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(profileId);
      Objects.requireNonNull(sessionId);

      //Send request
      return this.client()
        .put("/v1/session/" + profileId + "/" + sessionId, ImmutableSession.class, null);
    });
  }
}
