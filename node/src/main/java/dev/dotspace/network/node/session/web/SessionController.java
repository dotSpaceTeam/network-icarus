package dev.dotspace.network.node.session.web;

import dev.dotspace.network.library.session.ISession;
import dev.dotspace.network.node.session.db.SessionDatabase;
import dev.dotspace.network.node.web.AbstractRestController;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/session")
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
  public ResponseEntity<List<ISession>> getSessionList(@PathVariable @NotNull final String uniqueId) throws InterruptedException {
    return this.validateOkResponse(
      this.sessionDatabase.getSessionList(uniqueId),
      "Error while fetching sessionList=%s.".formatted(uniqueId));
  }

  /**
   * Create a session for uniqueId,
   */
  @GetMapping("/{uniqueId}/{sessionId}")
  @ResponseBody
  public ResponseEntity<ISession> getSessionList(@PathVariable @NotNull final String uniqueId,
                                                       @PathVariable @NotNull final Long sessionId) throws InterruptedException {
    return this.validateOkResponse(
      this.sessionDatabase.getSession(uniqueId, sessionId),
      "Error while fetching session=%s for uniqueId=%s.".formatted(sessionId, uniqueId));
  }

  /**
   * Create a session for uniqueId,
   */
  @PostMapping("/{uniqueId}")
  @ResponseBody
  public ResponseEntity<ISession> createSession(@PathVariable @NotNull final String uniqueId) throws InterruptedException {
    return this.validateOkResponse(
      this.sessionDatabase.createSession(uniqueId),
      "Error while creating session=%s.".formatted(uniqueId));
  }

  /**
   * Create a session for uniqueId,
   */
  @PutMapping("/{uniqueId}/{sessionId}")
  @ResponseBody
  public ResponseEntity<ISession> closeSession(@PathVariable @NotNull final String uniqueId,
                                               @PathVariable @NotNull final Long sessionId) throws InterruptedException {
    return this.validateOkResponse(
      this.sessionDatabase.completeSession(uniqueId, sessionId),
      "Error while closing session=%s for uniqueId=%s.".formatted(sessionId, uniqueId));
  }
}
