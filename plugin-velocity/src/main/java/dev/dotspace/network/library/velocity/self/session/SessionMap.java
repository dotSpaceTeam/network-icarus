package dev.dotspace.network.library.velocity.self.session;

import com.google.inject.Singleton;
import dev.dotspace.network.library.profile.session.ISession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * Store locale sessions current connected.
 */
@Singleton
public final class SessionMap implements ISessionMap {
  /**
   * Store sessions.
   */
  private final Map<String, ISession> sessionMap;

  /**
   * Construct empty.
   */
  public SessionMap() {
    this.sessionMap = new HashMap<>();
  }

  @Override
  public @NotNull ISessionMap add(@Nullable String uniqueId,
                                  @Nullable ISession session) {
    //Null check
    Objects.requireNonNull(uniqueId);
    Objects.requireNonNull(session);

    //Add to sessions
    this.sessionMap.put(uniqueId, session);

    return this;
  }

  @Override
  public @NotNull Optional<@Nullable ISession> removeAndGet(@Nullable String uniqueId) {
    //Null check
    Objects.requireNonNull(uniqueId);

    //Remove and return.
    return Optional.ofNullable(this.sessionMap.remove(uniqueId));
  }
}
