package dev.dotspace.network.library.session;

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
      description="Total time of all sessions for a client. (Time in ms)")
  long duration();
}
