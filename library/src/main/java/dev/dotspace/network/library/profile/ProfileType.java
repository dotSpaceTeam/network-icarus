package dev.dotspace.network.library.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public enum ProfileType {
  /**
   * If java profile
   */
  JAVA(0),
  /**
   * If profile is a bedrock profile.
   */
  BEDROCK(1);

  /**
   * If of type.
   */
  private final int id;

  //static

  /**
   * Get {@link ProfileType} from id. ({@link ProfileType#id})
   *
   * @param id to get type from.
   * @return present {@link ProfileType} owner of the id.
   * @throws IndexOutOfBoundsException if id is not a valid type.
   */
  public static @NotNull ProfileType fromId(final int id) throws IndexOutOfBoundsException {
    for (final ProfileType value : ProfileType.values()) { //Loop trough every type.
      if (value.id != id) { //Goto next enum value if no match.
        continue;
      }
      return value;
    }
    throw new IndexOutOfBoundsException("Given id has no matching type.");
  }
}
