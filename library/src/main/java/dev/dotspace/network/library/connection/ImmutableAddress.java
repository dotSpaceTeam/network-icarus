package dev.dotspace.network.library.connection;

import dev.dotspace.network.library.profile.IProfile;
import dev.dotspace.network.library.profile.ImmutableProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


//Swagger
@Schema(
    name="Address",
    description="Internet address of something. Mostly used for clients."
)
public record ImmutableAddress(@NotNull String address,
                               @NotNull InternetProtocolVersion version
) implements IAddress {
  /**
   * Convert {@link IAddress} to {@link ImmutableAddress}.
   *
   * @param address to convert.
   * @return instance of {@link IAddress}.
   */
  public static @NotNull IAddress of(@Nullable final IAddress address) {
    //Null check
    Objects.requireNonNull(address);

    return new ImmutableAddress(address.address(), address.version());
  }

  /**
   * Convert a {@link String} holding an address to and {@link IAddress}.
   *
   * @param address to convert.
   * @return instance of {@link IAddress}.
   */
  public static @NotNull IAddress of(@Nullable final String address) {
    //Null check
    Objects.requireNonNull(address);

    //Create immutable address.
    return new ImmutableAddress(address, InternetProtocolVersion.fromAddress(address));
  }
}
