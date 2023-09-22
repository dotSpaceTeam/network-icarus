package dev.dotspace.network.library.key;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;


/**
 * Apply a key to an object.
 */
//Swagger
@Schema(implementation=ImmutableKey.class)
public interface IKey {
  /**
   * Key of object as string.
   *
   * @return ey as string.
   */
  //Swagger
  @Schema(
      example="icarus.key",
      description="Key of object instance.")
  @NotNull String key();
}
