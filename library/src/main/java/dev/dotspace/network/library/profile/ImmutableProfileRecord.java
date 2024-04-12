package dev.dotspace.network.library.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


//Swagger
@Schema(
    name="ProfileRecord",
    description="Profile record value."
)
public record ImmutableProfileRecord(@NotNull String uniqueId,
                                     @NotNull String value
) implements IProfileRecord {
  /**
   * Convert {@link IProfileRecord} to {@link ImmutableProfileRecord}.
   *
   * @param profileRecord to convert.
   * @return instance of {@link IProfileRecord}.
   */
  public static @NotNull IProfileRecord of(@Nullable final IProfileRecord profileRecord) {
    //Null check
    Objects.requireNonNull(profileRecord);

    return new ImmutableProfileRecord(profileRecord.uniqueId(), profileRecord.value());
  }
}
