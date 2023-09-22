package dev.dotspace.network.node.session.web;

import dev.dotspace.network.library.session.ISession;
import dev.dotspace.network.library.session.ImmutableSession;
import dev.dotspace.network.node.exception.ElementException;
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

import java.util.List;


@RestController
@RequestMapping("/v1/session")
//Swagger
@Tag(name="Session", description="Manipulate and get player sessions.")
public final class SessionController extends AbstractRestController {
  /**
   * Manipulate database.
   */
  @Autowired
  private SessionDatabase sessionDatabase;

  /**
   * Create a session for uniqueId,
   */
  @GetMapping("/{uniqueId}")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get all stored sessions of client, use this function with caution.",
      description="Request every data for session stored, high data density.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Returns a list of every session.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableSession.class)
                  )
              })
      })
  public ResponseEntity<List<ISession>> getSessionList(@PathVariable @NotNull final String uniqueId) throws ElementException {
    return ResponseEntity.ok(this.sessionDatabase.getSessionList(uniqueId));
  }

  /**
   * Create a session for uniqueId,
   */
  @GetMapping("/{uniqueId}/{sessionId}")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get a specific stored session of client.",
      description="Request data for session stored.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return session with matching sessionId.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableSession.class)
                  )
              })
      })
  public ResponseEntity<ISession> getSessionList(@PathVariable @NotNull final String uniqueId,
                                                 @PathVariable @NotNull final Long sessionId) throws ElementNotPresentException {
    return ResponseEntity.ok(this.sessionDatabase.getSession(uniqueId, sessionId));
  }

  /**
   * Create a session for uniqueId,
   */
  @PostMapping("/{uniqueId}")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Create a new session for client.",
      description="Create a new session with no end timestamp.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return session created.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableSession.class)
                  )
              })
      })
  public ResponseEntity<ISession> createSession(@PathVariable @NotNull final String uniqueId) throws ElementException {
    return ResponseEntity.ok(this.sessionDatabase.createSession(uniqueId));
  }

  /**
   * Create a session for uniqueId,
   */
  @PutMapping("/{uniqueId}/{sessionId}")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Close a session for client with sessionId.",
      description="Set current timestamp as end for session.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return session closed.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableSession.class)
                  )
              })
      })
  public ResponseEntity<ISession> closeSession(@PathVariable @NotNull final String uniqueId,
                                               @PathVariable @NotNull final Long sessionId) throws ElementNotPresentException {
    return ResponseEntity.ok(this.sessionDatabase.completeSession(uniqueId, sessionId));
  }
}
