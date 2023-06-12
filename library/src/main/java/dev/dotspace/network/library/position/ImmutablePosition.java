package dev.dotspace.network.library.position;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Immutable instance of {@link IPosition}.
 */
public record ImmutablePosition(long x,
                                long y,
                                long z) implements IPosition {
  /**
   * Convert {@link IPosition} to {@link ImmutablePosition}.
   *
   * @param position to convert.
   * @return instance of {@link IPosition}.
   */
  public static @NotNull IPosition of(@Nullable final IPosition position) {
    //Null check
    Objects.requireNonNull(position);

    return new ImmutablePosition(position.x(), position.y(), position.z());
  }
}
