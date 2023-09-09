package dev.dotspace.network.library.profile.experience;

public interface IExperience {
  /**
   * Get experience points for object.
   *
   * @return amount as experience.
   */
  long experience();

  /**
   * Get level of experience.
   *
   * @return level for experience.
   */
  long level();
}
