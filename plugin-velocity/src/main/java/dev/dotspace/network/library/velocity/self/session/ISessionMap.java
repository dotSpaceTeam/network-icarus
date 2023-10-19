package dev.dotspace.network.library.velocity.self.session;

import dev.dotspace.network.library.profile.session.ISession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


/**
 * Store local sessions.
 */
public interface ISessionMap {
  /**
   * Add locale session.
   *
   * @param uniqueId to add session for.
   * @param session  id to add to player -> currently connected to network.
   * @return instance.
   * @throws NullPointerException if uniqueId or session is null.
   */
  @NotNull ISessionMap add(@Nullable final String uniqueId,
                           @Nullable final ISession session);

  /**
   * Remove session from list and return removed session.
   * Empty if no session was stored for uniqueId.
   *
   * @param uniqueId to store session for.
   * @return session.
   * @throws NullPointerException if uniqueId is null.
   */
  @NotNull Optional<@Nullable ISession> removeAndGet(@Nullable final String uniqueId);
}