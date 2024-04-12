package dev.dotspace.network.node.system.web;

import dev.dotspace.network.library.system.environment.ISystemEnvironment;
import dev.dotspace.network.library.system.environment.ImmutableSystemEnvironment;
import dev.dotspace.network.node.web.controller.AbstractRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller to configure client.
 */
@Log4j2
@RestController
@RequestMapping("/api/v1")
//Swagger
@Tag(name="Environment Endpoint", description="Get client environment.")
public final class EnvironmentController extends AbstractRestController {
  /**
   * Get environment.
   */
  @Autowired
  private ISystemEnvironment environment;

  /**
   * Get if system is running.
   */
  @GetMapping("/environment")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get environment necessary for clients to connect to system.",
      description="Node will send information for further interaction.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Receive environment of node.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableSystemEnvironment.class)
                  )
              })
      })
  public @NotNull ResponseEntity<ISystemEnvironment> getEnvironment() {
    //Send configuration to client.
    return ResponseEntity.ok(this.environment);
  }
}
