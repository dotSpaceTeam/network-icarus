package dev.dotspace.network.library.position;

import io.swagger.v3.oas.annotations.media.Schema;


/**
 * Base of {@link IPosition} and also yaw, pitch
 */
//Swagger
@Schema(implementation=ImmutableViewPosition.class)
public interface IViewPosition extends IPosition {
  /**
   * Get x of position.
   *
   * @return pos x.
   */
  //Swagger
  @Schema(example="128.0", description="Yaw coordinate of position.")
  double yaw();

  /**
   * Get y of position.
   *
   * @return pos y.
   */
  //Swagger
  @Schema(example="0.0", description="Pitch coordinate of position.")
  double pitch();
}
