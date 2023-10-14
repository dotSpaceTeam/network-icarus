package dev.dotspace.network.node.position.web;

import dev.dotspace.network.library.position.IPosition;
import dev.dotspace.network.library.position.IViewPosition;
import dev.dotspace.network.library.position.ImmutablePosition;
import dev.dotspace.network.library.position.ImmutableViewPosition;
import dev.dotspace.network.node.database.exception.EntityException;
import dev.dotspace.network.node.database.exception.EntityNotPresentException;
import dev.dotspace.network.node.position.db.PositionDatabase;
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
@RequestMapping("/api/v1/position")
//Swagger
@Tag(name="Position Endpoint", description="Manipulate and get positions.")
public final class PositionController extends AbstractRestController {
  /**
   * Position database.
   */
  @Autowired
  private PositionDatabase positionDatabase;

  /**
   * Insert an new profile from unique id.
   */
  @PutMapping
  @ResponseBody
  //Swagger
  @Operation(
      summary="Set position parameters on key.",
      description="Update or create position for key.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return created or updated position.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutablePosition.class)
                  )
              })
      })
  public ResponseEntity<IPosition> setPosition(@RequestBody @NotNull final ImmutablePosition immutablePosition) {
    return ResponseEntity.ok(this.positionDatabase.setPosition(
        immutablePosition.key(),
        immutablePosition.x(),
        immutablePosition.y(),
        immutablePosition.z()));
  }

  /**
   * Insert an new profile from unique id.
   */
  @GetMapping("/{key}")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get position from key.",
      description="Get position for key, if not present 404.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return present position.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutablePosition.class)
                  )
              })
      })
  public ResponseEntity<IPosition> getPosition(@PathVariable @NotNull final String key) throws EntityNotPresentException {
    return ResponseEntity.ok(this.positionDatabase.getPosition(key));
  }

  /**
   * Insert an new profile from unique id.
   */
  @PutMapping("/view")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Set view position parameters on key.",
      description="Update or view create position for key.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return created or updated view position.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableViewPosition.class)
                  )
              })
      })
  public ResponseEntity<IViewPosition> setViewPosition(@RequestBody @NotNull final ImmutableViewPosition immutableViewPosition,
                                                       @RequestParam(required=false) final boolean updateView) throws EntityException {
    //Update view coordination.
    if (updateView) {
      return ResponseEntity.ok(this.positionDatabase
          .setViewPosition(immutableViewPosition.key(), immutableViewPosition.yaw(), immutableViewPosition.pitch()));
    }

    //Create new
    return ResponseEntity.ok(this.positionDatabase.setViewPosition(
        immutableViewPosition.key(),
        immutableViewPosition.x(),
        immutableViewPosition.y(),
        immutableViewPosition.z(),
        immutableViewPosition.yaw(),
        immutableViewPosition.pitch()));
  }

  /**
   * Insert an new profile from unique id.
   */
  @GetMapping("/view/{key}")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get view position from key.",
      description="Get view position for key, if not present 404.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return present view position.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableViewPosition.class)
                  )
              })
      })
  public ResponseEntity<IViewPosition> getViewPosition(@PathVariable @NotNull final String key) throws EntityException {
    return ResponseEntity.ok(this.positionDatabase.getViewPosition(key));
  }
}
