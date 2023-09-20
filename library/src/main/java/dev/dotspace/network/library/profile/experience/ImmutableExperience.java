package dev.dotspace.network.library.profile.experience;

import dev.dotspace.network.library.profile.IProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


/**
 * Immutable implementation for {@link IExperience}.
 *///Swagger
@Schema(
    name="Experience",
    description="Experience pair for "
)
public record ImmutableExperience(@NotNull String name,
                                  long experience,
                                  long level
) implements IExperience {
  /**
   * Convert {@link IExperience} to {@link ImmutableExperience}.
   *
   * @param experience to convert.
   * @return instance of {@link IProfile}.
   */
  public static @NotNull IExperience of(@Nullable final IExperience experience) {
    //Null check
    Objects.requireNonNull(experience);

    return new ImmutableExperience(experience.name(), experience.experience(), experience.level());
  }

  /**
   * Convert experience to an {@link IExperience}. Level will be calculated trough levelFunction.
   *
   * @param experience         to use for {@link IExperience#experience()}.
   * @param experienceFunction to calculate {@link IExperience#level()}.
   * @return
   */
  public static @NotNull IExperience of(@Nullable final String name,
                                        final long experience,
                                        @Nullable final ExperienceFunction experienceFunction) {
    //Null check
    Objects.requireNonNull(name);
    Objects.requireNonNull(experienceFunction);

    return new ImmutableExperience(name, experience, experienceFunction.levelFromExperience(experience));
  }

}
