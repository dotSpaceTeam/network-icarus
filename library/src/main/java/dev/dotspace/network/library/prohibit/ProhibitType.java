package dev.dotspace.network.library.prohibit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
@Accessors(fluent=true)
public enum ProhibitType {
  /**
   * Kicks client, if online.
   */
  KICK(0),
  /**
   * Blocks connection at all.
   */
  BAN(1),
  /**
   * Block chat messages.
   */
  MUTE(2);

  /**
   * If of type.
   */
  private final int id;

  //static

  /**
   * Get {@link ProhibitType} from id. ({@link ProhibitType#id})
   *
   * @param id to get type from.
   * @return present {@link ProhibitType} owner of the id.
   * @throws IndexOutOfBoundsException if id is not a valid type.
   */
  public static @NotNull ProhibitType fromId(final int id) throws IndexOutOfBoundsException {
    for (final ProhibitType value : ProhibitType.values()) { //Loop trough every type.
      if (value.id != id) { //Goto next enum value if no match.
        continue;
      }
      return value;
    }
    throw new IndexOutOfBoundsException("Given id has no matching type.");
  }

}
