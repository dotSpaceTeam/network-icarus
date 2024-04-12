package dev.dotspace.network.library.position;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


/**
 * Immutable instance of {@link IPosition}.
 */
//Swagger
@Schema(
    name="Position",
    description="Position with x, y, z."
)
public record ImmutablePosition(@NotNull String key,
                                double x,
                                double y,
                                double z
) implements IPosition {
  /**
   * Convert {@link IPosition} to {@link ImmutablePosition}.
   *
   * @param position to convert.
   * @return instance of {@link IPosition}.
   */
  public static @NotNull IPosition of(@Nullable final IPosition position) {
    //Null check
    Objects.requireNonNull(position);

    return new ImmutablePosition(position.key(), position.x(), position.y(), position.z());
  }
}
