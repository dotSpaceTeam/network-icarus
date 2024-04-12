package dev.dotspace.network.library.profile.experience;

public interface ExperienceFunction {
  /**
   * Get level from experience.
   *
   * @param experience to convert into level.
   * @return solution of formular.
   */
  long levelFromExperience(final long experience);

}
