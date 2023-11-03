package dev.dotspace.network.library.system.participant;

import org.jetbrains.annotations.NotNull;


/**
 * Runtime info.
 */
public interface IParticipant {
  /**
   * Get type of runner.
   *
   * @return type of runner as {@link ParticipantType}.
   */
  @NotNull ParticipantType type();

  /**
   * Id of node. String of {@link java.util.UUID}.
   *
   * @return id as {@link String}.
   */
  @NotNull String identifier();
}
