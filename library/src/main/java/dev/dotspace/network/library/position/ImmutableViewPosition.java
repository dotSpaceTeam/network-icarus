package dev.dotspace.network.library.position;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


/**
 * Immutable instance of {@link IViewPosition}.
 */
//Swagger
@Schema(
    name="ViewPosition",
    description="Position with view angle."
)
public record ImmutableViewPosition(@NotNull String key,
                                    double x,
                                    double y,
                                    double z,
                                    double yaw,
                                    double pitch
) implements IViewPosition {
  /**
   * Convert {@link IViewPosition} to {@link ImmutableViewPosition}.
   *
   * @param viewPosition to convert.
   * @return instance of {@link IPosition}.
   */
  public static @NotNull IViewPosition of(@Nullable final IViewPosition viewPosition) {
    //Null check
    Objects.requireNonNull(viewPosition);

    //Clone
    return new ImmutableViewPosition(viewPosition.key(),
        viewPosition.x(),
        viewPosition.y(),
        viewPosition.z(),
        viewPosition.yaw(),
        viewPosition.pitch());
  }
}
