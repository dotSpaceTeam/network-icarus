package dev.dotspace.network.library.position;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Immutable instance of {@link IViewPosition}.
 */
public record ImmutableViewPosition(@NotNull String key,
                                    long x,
                                    long y,
                                    long z,
                                    long yaw,
                                    long pitch) implements IViewPosition {
  /**
   * Convert {@link IViewPosition} to {@link ImmutableViewPosition}.
   *
   * @param viewPosition to convert.
   * @return instance of {@link IPosition}.
   */
  public static @NotNull IViewPosition of(@Nullable final IViewPosition viewPosition) {
    //Null check
    Objects.requireNonNull(viewPosition);

    return new ImmutableViewPosition(viewPosition.key(), viewPosition.x(), viewPosition.y(), viewPosition.z(), viewPosition.yaw(), viewPosition.pitch());
  }
}
