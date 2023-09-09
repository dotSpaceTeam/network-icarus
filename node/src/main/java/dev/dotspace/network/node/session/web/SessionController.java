package dev.dotspace.network.node.session.web;

import dev.dotspace.network.library.session.ISession;
import dev.dotspace.network.node.exception.ElementException;
import dev.dotspace.network.node.exception.ElementNotPresentException;
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
  public ResponseEntity<List<ISession>> getSessionList(@PathVariable @NotNull final String uniqueId) throws ElementException {
    return ResponseEntity.ok(this.sessionDatabase.getSessionList(uniqueId));
  }

  /**
   * Create a session for uniqueId,
   */
  @GetMapping("/{uniqueId}/{sessionId}")
  @ResponseBody
  public ResponseEntity<ISession> getSessionList(@PathVariable @NotNull final String uniqueId,
                                                 @PathVariable @NotNull final Long sessionId) throws ElementNotPresentException {
    return ResponseEntity.ok(this.sessionDatabase.getSession(uniqueId, sessionId));
  }

  /**
   * Create a session for uniqueId,
   */
  @PostMapping("/{uniqueId}")
  @ResponseBody
  public ResponseEntity<ISession> createSession(@PathVariable @NotNull final String uniqueId) throws ElementException {
    return ResponseEntity.ok(this.sessionDatabase.createSession(uniqueId));
  }

  /**
   * Create a session for uniqueId,
   */
  @PutMapping("/{uniqueId}/{sessionId}")
  @ResponseBody
  public ResponseEntity<ISession> closeSession(@PathVariable @NotNull final String uniqueId,
                                               @PathVariable @NotNull final Long sessionId) throws ElementNotPresentException {
    return ResponseEntity.ok(this.sessionDatabase.completeSession(uniqueId, sessionId));
  }
}
