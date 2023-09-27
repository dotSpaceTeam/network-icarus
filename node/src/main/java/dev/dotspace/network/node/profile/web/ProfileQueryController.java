package dev.dotspace.network.node.profile.web;

import dev.dotspace.network.library.profile.IProfileRecord;
import dev.dotspace.network.library.profile.ImmutableProfileRecord;
import dev.dotspace.network.node.exception.ElementException;
import dev.dotspace.network.node.profile.db.ProfileDatabase;
import dev.dotspace.network.node.web.AbstractRestController;
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
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping(value="/v1/profile-query")
//Swagger
@Tag(name="Profile-Queries", description="Query for profile parameters.")
public final class ProfileQueryController extends AbstractRestController {
  /**
   * Profile database.
   */
  @Autowired
  private ProfileDatabase profileDatabase;

  /**
   * Get an profile from name.
   */
  @GetMapping("/uniqueIdFromName/{name}")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get record with name and requested uniqueId.",
      description="Return record associated to name, null if absent.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="UniqueId and name if present. (not case sensitive.)",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableProfileRecord.class)
                  )
              })
      })
  public ResponseEntity<IProfileRecord> getUniqueIdFromName(@PathVariable @NotNull final String name) throws ElementException {
    return ResponseEntity.ok(this.profileDatabase.uniqueIdFromName(name));
  }

  /**
   * Get an profile from unique id.
   */
  @GetMapping("/nameFromUniqueId/{uniqueId}")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get record with uniqueId and requested name.",
      description="Return record associated to uniqueId, null if absent.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="UniqueId and name if present.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableProfileRecord.class)
                  )
              })
      })
  public ResponseEntity<IProfileRecord> getNameFromUniqueId(@PathVariable @NotNull final String uniqueId) throws ElementException {
    return ResponseEntity.ok(this.profileDatabase.nameFromUniqueId(uniqueId));
  }
}