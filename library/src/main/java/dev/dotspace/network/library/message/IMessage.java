package dev.dotspace.network.library.message;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import org.jetbrains.annotations.NotNull;


/**
 * Wrap for string message.
 */
//Swagger
@Schema(implementation=ImmutableMessage.class)
public interface IMessage {
  /**
   * String message to use directly as message.
   *
   * @return String message.
   */
  //Swagger
  @Schema(
      example="Today is a good day! :D",
      description="Message content for object instance."
  )
  @NotNull String message();
}
