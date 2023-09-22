package dev.dotspace.network.library.state;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;


public interface IState<TYPE> {
  /**
   * State.
   *
   * @return type of TYPE.
   */
  //Swagger
  @Schema(description="Stored value for state.")
  @NotNull TYPE state();

  /**
   * Info message to current state.
   *
   * @return message.
   */
  //Swagger
  @Schema(description="Message for stored state.")
  @NotNull String message();
}
