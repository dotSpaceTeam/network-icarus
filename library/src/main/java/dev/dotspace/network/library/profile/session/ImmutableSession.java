package dev.dotspace.network.library.profile.session;

import dev.dotspace.network.library.connection.IAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Objects;


@Schema(
    name="Session",
    description="Duration a player has connected and disconnected from the network."
)
public record ImmutableSession(@NotNull Long sessionId,
                               @NotNull Date startDate,
                               @NotNull Date endDate,
                               long duration,
                               @NotNull IAddress connectionAddress
                               ) implements ISession {
  /**
   * Convert {@link ISession} to {@link ImmutableSession}.
   *
   * @param session to convert.
   * @return instance of {@link ImmutableSession}.
   */
  public static @NotNull ISession of(@Nullable final ISession session) {
    //Null check
    Objects.requireNonNull(session);

    return new ImmutableSession(session.sessionId(),
        session.startDate(),
        session.endDate(),
        session.duration(),
        session.connectionAddress());
  }
}
