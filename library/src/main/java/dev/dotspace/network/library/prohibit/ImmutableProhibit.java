package dev.dotspace.network.library.prohibit;

import dev.dotspace.network.library.profile.IProfile;
import jakarta.validation.constraints.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public record ImmutableProhibit(@NotNull IProhibitReason reason,
                                @NotNull IProfile punishedProfile,
                                @NotNull IProfile executorProfile,
                                @NotNull String message,
                                boolean active
) implements IProhibit {
  /**
   * Convert {@link IProhibit} to {@link ImmutableProhibit}.
   *
   * @param prohibit to convert.
   * @return instance of {@link IProhibit}.
   */
  public static @NotNull IProhibit of(@Nullable final IProhibit prohibit) {
    //Null check
    Objects.requireNonNull(prohibit);

    return new ImmutableProhibit(prohibit.reason(),
        prohibit.punishedProfile(),
        prohibit.executorProfile(),
        prohibit.message(),
        prohibit.active());
  }
}
