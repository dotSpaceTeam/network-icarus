package dev.dotspace.network.library.profile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * Immutable instance of profile.
 *
 * @param uniqueId
 * @param profileType
 * @param attributes
 */
public record ImmutableCombinedProfile(@NotNull String uniqueId,
                                       @NotNull ProfileType profileType,
                                       @NotNull List<IProfileAttribute> attributes) implements ICombinedProfile {


  /**
   * Convert {@link ICombinedProfile} to {@link ImmutableCombinedProfile}.
   *
   * @param combinedProfile to convert.
   * @return instance of {@link ICombinedProfile}.
   */
  public static @NotNull ICombinedProfile of(@Nullable final ICombinedProfile combinedProfile) {
    //Null check
    Objects.requireNonNull(combinedProfile);

    return new ImmutableCombinedProfile(combinedProfile.uniqueId(), combinedProfile.profileType(), combinedProfile.attributes());
  }
}
