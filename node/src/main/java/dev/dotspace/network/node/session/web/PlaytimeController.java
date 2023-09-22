package dev.dotspace.network.node.session.web;

import dev.dotspace.network.library.profile.ImmutableProfile;
import dev.dotspace.network.library.session.IPlaytime;
import dev.dotspace.network.library.session.ImmutablePlaytime;
import dev.dotspace.network.node.exception.ElementNotPresentException;
import dev.dotspace.network.node.session.db.SessionDatabase;
import dev.dotspace.network.node.web.AbstractRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/playtime")
//Swagger
@Tag(name="Session-Playtime", description="Get total playtime of clientId.")
public final class PlaytimeController extends AbstractRestController {
  /**
   * Manipulate database.
   */
  @Autowired
  private SessionDatabase sessionDatabase;

  /**
   * Get playtime of uniqueId
   */
  @GetMapping("/{uniqueId}")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get total time a client played on server in milliseconds.",
      description="Returns the calculated time of player been connected to network.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Final playtime is calculated from all session durations. Session need to be closed to "+
                  "count to playtime.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutablePlaytime.class)
                  )
              })
      })
  public ResponseEntity<IPlaytime> getPlaytime(@PathVariable @NotNull final String uniqueId) throws ElementNotPresentException {
    return ResponseEntity.ok(this.sessionDatabase.getPlaytime(uniqueId));
  }
}