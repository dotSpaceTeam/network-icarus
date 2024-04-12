package dev.dotspace.network.library.profile.experience;

import dev.dotspace.network.library.common.Nameable;
import io.swagger.v3.oas.annotations.media.Schema;


//Swagger
@Schema(implementation=ImmutableExperience.class)
public interface IExperience extends Nameable {

  /**
   * Get experience points for object.
   *
   * @return amount as experience.
   */
  @Schema(example="1000", description="Amount of experience points included.")
  long experience();

  /**
   * Get level of experience.
   *
   * @return level for experience.
   */
  @Schema(example="1", description="Level calculated from experience with an formula. (1 Level equals 1000 exp.)")
  long level();
}
