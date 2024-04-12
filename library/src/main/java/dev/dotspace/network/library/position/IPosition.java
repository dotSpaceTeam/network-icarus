package dev.dotspace.network.library.position;

import dev.dotspace.network.library.key.IKey;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * Position store x,y,z
 */
//Swagger
@Schema(implementation=ImmutablePosition.class)
public interface IPosition extends IKey {
  /**
   * Get x of position.
   *
   * @return pos x.
   */
  //Swagger
  @Schema(example="5.5", description="X coordinate of position.")
  double x();

  /**
   * Get y of position.
   *
   * @return pos y.
   */
  //Swagger
  @Schema(example="2.5", description="Y coordinate of position.")
  double y();

  /**
   * Get z of position.
   *
   * @return pos z.
   */
  //Swagger
  @Schema(example="1.25", description="Z coordinate of position.")
  double z();
}
