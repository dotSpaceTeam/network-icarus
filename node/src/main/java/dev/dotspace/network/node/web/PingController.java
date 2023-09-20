package dev.dotspace.network.node.web;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1")
public final class PingController {
  /**
   * Get if system is running.
   */
  @GetMapping("/ping")
  @ResponseBody
  public @NotNull ResponseEntity<Boolean> ping() {
    //Webserver is online
    return ResponseEntity.ok(true);
  }
}
