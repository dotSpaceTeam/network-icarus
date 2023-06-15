package dev.dotspace.network.library.session;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;

public record ImmutableSession(@NotNull Date startDate,
                               @NotNull Date endDate,
                               @NotNull Duration duration) implements ISession {
  /**
   * Convert {@link ISession} to {@link ImmutableSession}.
   *
   * @param session to convert.
   * @return instance of {@link ImmutableSession}.
   */
  public static @NotNull ISession of(@Nullable final ISession session) {
    //Null check
    Objects.requireNonNull(session);

    return new ImmutableSession(session.startDate(), session.endDate(), session.duration());
  }
}
