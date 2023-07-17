package dev.dotspace.network.node.web;

import dev.dotspace.network.library.state.ImmutableBooleanState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public final class RunningController {

  /**
   * Get if system is running.
   */
  @GetMapping("/running")
  @ResponseBody
  public ResponseEntity<ImmutableBooleanState> getRunning() {
    return ResponseEntity.ok(new ImmutableBooleanState(true, "System up and running."));
  }
}
