package dev.dotspace.network.library.connection;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


//Swagger
@Schema(
    name="AddressName",
    description="Internet address of something. "
)
public record ImmutableAddressName(@NotNull String address) implements IAddressName {
  /**
   * Convert {@link IAddressName} to {@link ImmutableAddressName}.
   *
   * @param addressName to convert.
   * @return instance of {@link IAddressName}.
   */
  public static @NotNull IAddressName of(@Nullable final IAddressName addressName) {
    //Null check
    Objects.requireNonNull(addressName);

    return new ImmutableAddressName(addressName.address());
  }
}
