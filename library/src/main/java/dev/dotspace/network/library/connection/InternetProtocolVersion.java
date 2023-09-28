package dev.dotspace.network.library.connection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


/**
 * Version of ip protocol. Mostly used in combination of ip.
 */
@Getter
@AllArgsConstructor()
@Accessors(fluent=true)
public enum InternetProtocolVersion {
  /**
   * No version was defined.
   */
  UNDEFINED(0),
  /**
   * If ip is stored as version 4.
   */
  VERSION_4(1),
  /**
   * If ip is stored as version 6.
   */
  VERSION_6(2);

  /**
   * Id of version.
   */
  private final int id;

  //static

  /**
   * Get {@link InternetProtocolVersion} from id. ({@link InternetProtocolVersion#id})
   *
   * @param id to get version from.
   * @return present {@link InternetProtocolVersion} owner of the id.
   * @throws IndexOutOfBoundsException if id is not a valid type.
   */
  public static @NotNull InternetProtocolVersion fromId(final int id) throws IndexOutOfBoundsException {
    for (final InternetProtocolVersion value : InternetProtocolVersion.values()) { //Loop trough every type.
      if (value.id != id) { //Goto next enum value if no match.
        continue;
      }
      return value;
    }
    throw new IndexOutOfBoundsException("Given id has no matching type.");
  }

  /**
   * Get protocol version from address.
   *
   * @param address to get version from.
   * @return version address is written in.
   */
  public static @NotNull InternetProtocolVersion fromAddress(@Nullable final String address) {
    //Null check
    Objects.requireNonNull(address);

    //Calculate internet version.
    if (address.contains(":")) {
      //If : present -> version 6
      return InternetProtocolVersion.VERSION_6;
    } else if (address.contains(".")) {
      //If . present -> version 4
      return InternetProtocolVersion.VERSION_4;
    }
    //If none of the other match using undefined.
    return InternetProtocolVersion.UNDEFINED;
  }
}