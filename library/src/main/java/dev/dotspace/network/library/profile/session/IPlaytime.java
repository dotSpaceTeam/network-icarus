package dev.dotspace.network.library.profile.session;

import io.swagger.v3.oas.annotations.media.Schema;


//Swagger
@Schema(implementation=ImmutablePlaytime.class)
public interface IPlaytime {
  /**
   * Duration between two values.
   *
   * @return difference.
   */
  //Swagger
  @Schema(
      example="20000",
      description="Total time of session. (Time in ms)")
  long duration();
}
