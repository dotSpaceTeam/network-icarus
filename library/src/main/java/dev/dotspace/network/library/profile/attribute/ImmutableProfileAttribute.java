package dev.dotspace.network.library.profile.attribute;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record ImmutableProfileAttribute(@NotNull String key,
                                        @NotNull String value) implements IProfileAttribute {
  /**
   * Convert {@link IProfileAttribute} to {@link ImmutableProfileAttribute}.
   *
   * @param profileAttribute to convert.
   * @return instance of {@link ImmutableProfileAttribute}.
   */
  public static @NotNull IProfileAttribute of(@Nullable final IProfileAttribute profileAttribute) {
    //Null check
    Objects.requireNonNull(profileAttribute);

    return new ImmutableProfileAttribute(profileAttribute.key(), profileAttribute.value());
  }
}
