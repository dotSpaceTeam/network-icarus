package dev.dotspace.network.node.prohibit.web;

import dev.dotspace.network.library.prohibit.IProhibitReason;
import dev.dotspace.network.library.prohibit.ImmutableProhibitReason;
import dev.dotspace.network.node.database.exception.IllegalEntityException;
import dev.dotspace.network.node.prohibit.db.ProhibitDatabase;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/prohibit")
//Swagger
@Tag(name="Prohibit Endpoint", description="Manipulate and get positions.")
public final class ProhibitController extends AbstractRestController {
  /**
   * Position database.
   */
  @Autowired
  private ProhibitDatabase prohibitDatabase;

  /**
   * Insert or update reason
   */
  @PostMapping("/reason")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Update prohibit reason.",
      description="Update or create new reason. If reason is already present only title and description will be updated.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return created or updated prohibit.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableProhibitReason.class)
                  )
              })
      })
  public ResponseEntity<IProhibitReason> setPosition(@RequestBody @NotNull final ImmutableProhibitReason immutableProhibitReason) throws IllegalEntityException {
    return ResponseEntity.ok(this.prohibitDatabase.updateReason(immutableProhibitReason.type(),
        immutableProhibitReason.name(),
        immutableProhibitReason.title(),
        immutableProhibitReason.description()));
  }
}
