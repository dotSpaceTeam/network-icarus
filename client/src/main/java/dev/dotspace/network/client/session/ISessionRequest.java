package dev.dotspace.network.client.session;

import dev.dotspace.common.response.Response;
import dev.dotspace.network.library.session.IPlaytime;
import dev.dotspace.network.library.session.ISession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * Request for {@link ISession}.
 */
public interface ISessionRequest {
  /**
   * Get Session from id and profile.
   *
   * @param uniqueId to get specific session.
   * @param sessionId id of session.
   * @return matching {@link ISession} of profileId and sessionId.
   */
  @NotNull Response<ISession> getSession(@Nullable final String uniqueId,
                                         @Nullable final Long sessionId);

  /**
   * Calculate playtime of profile id.
   *
   * @param uniqueId to calculate playtime for.
   * @return calculated play time of profile.
   */
  @NotNull Response<IPlaytime> getPlaytime(@Nullable final String uniqueId);

  /**
   * Create new session for profile.
   *
   * @param uniqueId to create new session for.
   * @return created {@link ISession}.
   */
  @NotNull Response<ISession> createSession(@Nullable final String uniqueId);

  /**
   * Complete a session of profile.
   *
   * @param uniqueId to complete session for.
   * @param sessionId to complete.
   * @return completed {@link ISession}.
   */
  @NotNull Response<ISession> completeSession(@Nullable final String uniqueId,
                                              @Nullable final Long sessionId);
}
