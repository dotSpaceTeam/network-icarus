package dev.dotspace.network.node.prohibit.web;

import dev.dotspace.network.library.prohibit.IProhibitReason;
import dev.dotspace.network.library.prohibit.ImmutableProhibitReason;
import dev.dotspace.network.node.database.exception.IllegalEntityException;
import dev.dotspace.network.node.database.request.ListSplitter;
import dev.dotspace.network.node.prohibit.db.ProhibitDatabase;
import dev.dotspace.network.node.web.AbstractRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/prohibit")
//Swagger
@Tag(name="Prohibit Endpoint", description="Manipulate and get prohibit and reasons.")
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
  public ResponseEntity<IProhibitReason> updateProhibit(@RequestBody @NotNull final ImmutableProhibitReason immutableProhibitReason)
      throws IllegalEntityException {
    return ResponseEntity.ok(this.prohibitDatabase.updateReason(immutableProhibitReason.type(),
        immutableProhibitReason.name(),
        immutableProhibitReason.title(),
        immutableProhibitReason.description()));
  }

  /**
   * Get list of all present reasons.
   */
  @GetMapping("/reason")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get all present reasons.",
      description="Get all stored present reasons stored.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Reasons as list.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableProhibitReason.class)
                  )
              })
      })
  public @NotNull ResponseEntity<List<IProhibitReason>> getProhibitList(@RequestParam(required=false) @Nullable final String listPattern) {
    return ResponseEntity.ok(new ListSplitter<IProhibitReason>(listPattern)
        //Handle if patern is pagination with or without sort
        .pagination(this.prohibitDatabase::getReasonList)
        //If list should be sorted.
        .sort(this.prohibitDatabase::getReasonList)
        //If no pattern is defined.
        .none(this.prohibitDatabase::getReasonList)
        //Run search.
        .execute());
  }
}
