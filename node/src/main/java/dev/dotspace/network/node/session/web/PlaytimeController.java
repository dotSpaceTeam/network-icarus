package dev.dotspace.network.node.session.web;

import dev.dotspace.network.library.session.IPlaytime;
import dev.dotspace.network.node.session.db.SessionDatabase;
import dev.dotspace.network.node.web.AbstractRestController;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/playtime")
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
  public ResponseEntity<IPlaytime> getPlaytime(@PathVariable @NotNull final String uniqueId) throws InterruptedException {
    return this.validateOkResponse(this.sessionDatabase.getPlaytime(uniqueId),
      "Error while fetching playtime=%s.".formatted(uniqueId));
  }
}