package dev.dotspace.network.library.system;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;


@Log4j2
public record ImmutableParticipant(@NotNull ParticipantType type,
                                   @NotNull String identifier
) implements IParticipant {
  /**
   * Convert {@link IParticipant} to {@link ImmutableParticipant}.
   *
   * @param participant to convert.
   * @return instance of {@link IParticipant}.
   */
  public static @NotNull IParticipant of(@Nullable final IParticipant participant) {
    //Null check
    Objects.requireNonNull(participant);

    return new ImmutableParticipant(participant.type(), participant.identifier());
  }

  /**
   * Create Random runtime of parameters.
   *
   * @param type to create random runtime.
   * @return instance of {@link IParticipant}.
   */
  public static @NotNull IParticipant randomOfType(@Nullable final ParticipantType type) {
    //Null check
    Objects.requireNonNull(type);

    final ImmutableParticipant participant = new ImmutableParticipant(type, UUID.randomUUID().toString());

    log.info("System defined as type={} and id={}.", participant.type(), participant.identifier());

    return participant;
  }
}
