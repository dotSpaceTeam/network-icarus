package dev.dotspace.network.library.profile;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


//Swagger
@Schema(
    name="Profile",
    description="Base profile. Pair of id and type of profile."
)
public record ImmutableProfile(@NotNull String uniqueId,
                               @NotNull String name,
                               @NotNull ProfileType profileType
) implements IProfile {
  /**
   * Empty profile -> use if not present.
   */
  private static final @NotNull IProfile SYSTEM = new ImmutableProfile("~SYSTEM~", "~SYSTEM~", ProfileType.JAVA);

  //static
  public static @NotNull IProfile system() {
    return SYSTEM;
  }

  /**
   * Convert {@link IProfile} to {@link ImmutableProfile}.
   *
   * @param profile to convert.
   * @return instance of {@link IProfile}.
   */
  public static @NotNull IProfile of(@Nullable final IProfile profile) {
    //Null check
    Objects.requireNonNull(profile);

    return new ImmutableProfile(profile.uniqueId(), profile.name(), profile.profileType());
  }
}
