package dev.dotspace.network.library.profile.experience;

import dev.dotspace.network.library.common.Nameable;


public interface IExperience extends Nameable {

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
