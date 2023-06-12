package dev.dotspace.network.node.position.web;

import dev.dotspace.network.library.position.IPosition;
import dev.dotspace.network.library.position.ImmutablePosition;
import dev.dotspace.network.library.profile.IProfile;
import dev.dotspace.network.library.profile.ImmutableProfile;
import dev.dotspace.network.node.position.db.PositionDatabase;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/v1/position")
public final class PositionController {
  /**
   * Position database.
   */
  @Autowired
  private PositionDatabase positionDatabase;

  /**
   * Insert an new profile from unique id.
   */
  @PutMapping()
  @ResponseBody
  public ResponseEntity<IPosition> setPosition(@RequestBody @NotNull final ImmutablePosition immutablePosition) throws InterruptedException {
    return ResponseEntity.ok(Objects.requireNonNull(
      this.positionDatabase.setPosition(
        Objects.requireNonNull(immutablePosition.key(), "Key of body is null"),
        immutablePosition.x(),
        immutablePosition.y(),
        immutablePosition.z()).get(),
      "Position error '%s'".formatted(immutablePosition.key())));
  }

  /**
   * Insert an new profile from unique id.
   */
  @GetMapping("/{key}")
  @ResponseBody
  public ResponseEntity<IPosition> getPosition(@PathVariable @NotNull final String key) throws InterruptedException {
    return ResponseEntity.ok(Objects.requireNonNull(
      this.positionDatabase.getPosition(key).get(),
      "Position '%s' not found".formatted(key)));

  }
}
