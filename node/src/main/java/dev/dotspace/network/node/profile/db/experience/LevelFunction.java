package dev.dotspace.network.node.profile.db.experience;


import dev.dotspace.network.library.profile.experience.ExperienceFunction;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LevelFunction implements ExperienceFunction {
  @Getter
  @Accessors(fluent = true)
  private final static LevelFunction function = new LevelFunction();

  @Override
  public long levelFromExperience(long experience) {
    return experience;
  }
}
