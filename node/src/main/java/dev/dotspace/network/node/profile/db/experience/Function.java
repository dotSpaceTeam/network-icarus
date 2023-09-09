package dev.dotspace.network.node.profile.db.experience;


import dev.dotspace.network.library.profile.experience.ExperienceFunction;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class Function implements ExperienceFunction {
  @Getter
  @Accessors(fluent = true)
  private final static Function function = new Function();

  @Override
  public long levelFromExperience(long experience) {
    return experience;
  }
}
