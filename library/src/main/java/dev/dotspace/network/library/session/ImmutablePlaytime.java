package dev.dotspace.network.library.session;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

//Swagger
@Schema(
    name="Playtime",
    description="Time a specific player played on"
)
public record ImmutablePlaytime(long duration) implements IPlaytime {
  /**
   * Convert {@link IPlaytime} to {@link ImmutablePlaytime}.
   *
   * @param playtime to convert.
   * @return instance of {@link ImmutablePlaytime}.
   */
  public static @NotNull IPlaytime of(@Nullable final IPlaytime playtime) {
    //Null check
    Objects.requireNonNull(playtime);

    return new ImmutablePlaytime(playtime.duration());
  }

  /**
   * Create from duration.
   */
  public static @NotNull IPlaytime with(final long duration) {
    return new ImmutablePlaytime(duration);
  }
}
