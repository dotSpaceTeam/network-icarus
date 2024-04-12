package dev.dotspace.network.node.web.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;


/**
 * Response from server
 * @param <TYPE> generic type of object
 */
public interface IBodyResponse<TYPE> extends IResponse {
  /**
   * Content of response.
   * @return content
   */
  //Swagger
  @Schema(example="{ \"key\": \"value\" }", description="Object of response.")
  @NotNull TYPE body();
}
