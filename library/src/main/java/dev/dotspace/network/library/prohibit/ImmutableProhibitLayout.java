package dev.dotspace.network.library.prohibit;

import jakarta.validation.constraints.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public record ImmutableProhibitLayout(@NotNull String reason,
                                      @NotNull String punishedProfile,
                                      @NotNull String executorProfile,
                                      @NotNull String message
) implements IProhibitLayout {
  /**
   * Convert {@link IProhibitLayout} to {@link ImmutableProhibitLayout}.
   *
   * @param prohibitLayout to convert.
   * @return instance of {@link IProhibit}.
   */
  public static @NotNull IProhibitLayout of(@Nullable final IProhibitLayout prohibitLayout) {
    //Null check
    Objects.requireNonNull(prohibitLayout);

    return new ImmutableProhibitLayout(prohibitLayout.reason(),
        prohibitLayout.punishedProfile(),
        prohibitLayout.executorProfile(),
        prohibitLayout.message());
  }
}
