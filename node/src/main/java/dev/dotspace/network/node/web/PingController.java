package dev.dotspace.network.node.web;

import dev.dotspace.network.library.state.ImmutableBooleanState;
import dev.dotspace.network.library.system.participant.IParticipant;
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


@Log4j2
@RestController
@RequestMapping("/api/v1")
//Swagger
@Tag(name="Ping Endpoint", description="Ping node instance.")
public final class PingController {
  /**
   * Get local participant
   */
  @Autowired
  private IParticipant participant;

  /**
   * Get if system is running.
   */
  @GetMapping("/ping")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Ping node and wait for response.",
      description="If node answers with code 200, everything is up and running.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Receive state of node.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableBooleanState.class)
                  )
              })
      })
  public @NotNull ResponseEntity<ImmutableBooleanState> ping() {
    //Webserver is online
    return ResponseEntity
        .ok(new ImmutableBooleanState(true, "Node="+this.participant.identifier()+" is running."));
  }
}
