package dev.dotspace.network.library.profile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record ImmutableProfile(@NotNull String uniqueId,
                               @NotNull ProfileType profileType) implements IProfile {
  /**
   * Convert {@link IProfile} to {@link ImmutableProfile}.
   *
   * @param profile to convert.
   * @return instance of {@link IProfile}.
   */
  public static @NotNull IProfile of(@Nullable final IProfile profile) {
    //Null check
    Objects.requireNonNull(profile);

    return new ImmutableProfile(profile.uniqueId(), profile.profileType());
  }
}
