package dev.dotspace.network.library.time;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Objects;


//Swagger
@Schema(
    name="Timestamp",
    description="Object stores a given timestamp."
)
public record ImmutableTimestamp(@NotNull Date timestamp) implements ITimestamp {
  /**
   * Convert {@link ITimestamp} to {@link ImmutableTimestamp}.
   *
   * @param timestamp to convert.
   * @return instance of {@link ITimestamp}.
   */
  public static @NotNull ITimestamp of(@Nullable final ITimestamp timestamp) {
    //Null check
    Objects.requireNonNull(timestamp);

    return new ImmutableTimestamp(timestamp.timestamp());
  }
}
