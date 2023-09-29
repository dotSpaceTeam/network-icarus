package dev.dotspace.network.library.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

/**
 * Type of runner.
 */
@Getter
@AllArgsConstructor()
@Accessors(fluent = true)
public enum ParticipantType {
  /**
   * If runner is a client.
   */
  CLIENT(0),
  /**
   * If runner is a node.
   */
  NODE(1);

  /**
   * If of type.
   */
  private final int id;

  //static

  /**
   * Get {@link ParticipantType} from id. ({@link ParticipantType#id})
   *
   * @param id to get type from.
   * @return present {@link ParticipantType} owner of the id.
   * @throws IndexOutOfBoundsException if id is not a valid type.
   */
  public static @NotNull ParticipantType fromId(final int id) throws IndexOutOfBoundsException {
    for (final ParticipantType value : ParticipantType.values()) { //Loop trough every type.
      if (value.id != id) { //Goto next enum value if no match.
        continue;
      }
      return value;
    }
    throw new IndexOutOfBoundsException("Given id has no matching type.");
  }
}
