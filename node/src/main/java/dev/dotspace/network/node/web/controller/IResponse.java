package dev.dotspace.network.node.web.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * Response from server
 */
public interface IResponse {
  /**
   * Time on response send by server.
   *
   * @return time in ms.
   */
  //Swagger
  @Schema(example="1695862448", description="Unix timestamp of response.")
  long timestamp();

  /**
   * Status code of response.
   *
   * @return code state of response.
   */
  //Swagger
  @Schema(example="200", description="Status code of response.")
  int code();

  /**
   * String message to code.
   *
   * @return message of response.
   */
  //Swagger
  @Schema(example="Ok", description="Message to code response.")
  @NotNull String message();
}
