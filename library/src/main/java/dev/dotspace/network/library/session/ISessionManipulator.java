package dev.dotspace.network.library.session;

import dev.dotspace.common.response.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Manipulate {@link ISession} objects in storage.
 */
public interface ISessionManipulator {
  /**
   * Get all sessions of profile id.
   *
   * @param profileId to get session list from.
   * @return list of all {@link ISession} entities for profile.
   */
  @NotNull Response<List<ISession>> getSessionList(@Nullable final String profileId);

  /**
   * Get Session from id and profile.
   *
   * @param profileId to get specific session.
   * @param sessionId id of session.
   * @return matching {@link ISession} of profileId and sessionId.
   */
  @NotNull Response<ISession> getSession(@Nullable final String profileId,
                                                    @Nullable final Long sessionId);

  /**
   * Calculate playtime of profile id.
   *
   * @param profileId to calculate playtime for.
   * @return calculated play time of profile.
   */
  @NotNull Response<IPlaytime> getPlaytime(@Nullable final String profileId);

  /**
   * Create new session for profile.
   *
   * @param profileId to create new session for.
   * @return created {@link ISession}.
   */
  @NotNull Response<ISession> createSession(@Nullable final String profileId);

  /**
   * Complete a session of profile.
   *
   * @param profileId to complete session for.
   * @param sessionId to complete.
   * @return completed {@link ISession}.
   */
  @NotNull Response<ISession> completeSession(@Nullable final String profileId,
                                                         @Nullable final Long sessionId);
}
