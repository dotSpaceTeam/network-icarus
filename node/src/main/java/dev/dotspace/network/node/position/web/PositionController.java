package dev.dotspace.network.node.position.web;

import dev.dotspace.network.library.position.IPosition;
import dev.dotspace.network.library.position.IViewPosition;
import dev.dotspace.network.library.position.ImmutablePosition;
import dev.dotspace.network.library.position.ImmutableViewPosition;
import dev.dotspace.network.node.exception.ElementException;
import dev.dotspace.network.node.exception.ElementNotPresentException;
import dev.dotspace.network.node.position.db.PositionDatabase;
import dev.dotspace.network.node.web.AbstractRestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
@RequestMapping("/api/v1/position")
//Swagger
@Tag(name="Position Endpoint", description="Manipulate and get positions.")
public final class PositionController extends AbstractRestController {
  /**
   * Position database.
   */
  @Autowired
  private PositionDatabase positionDatabase;

  /**
   * Insert an new profile from unique id.
   */
  @PutMapping("/")
  @ResponseBody
  public ResponseEntity<IPosition> setPosition(@RequestBody @NotNull final ImmutablePosition immutablePosition) {
    return ResponseEntity.ok(this.positionDatabase.setPosition(
        immutablePosition.key(),
        immutablePosition.x(),
        immutablePosition.y(),
        immutablePosition.z()));
  }

  /**
   * Insert an new profile from unique id.
   */
  @GetMapping("/{key}")
  @ResponseBody
  public ResponseEntity<IPosition> getPosition(@PathVariable @NotNull final String key) throws ElementNotPresentException {
    return ResponseEntity.ok(this.positionDatabase.getPosition(key));
  }

  /**
   * Insert an new profile from unique id.
   */
  @PutMapping("/view/")
  @ResponseBody
  public ResponseEntity<IViewPosition> setViewPosition(@RequestBody @NotNull final ImmutableViewPosition immutableViewPosition,
                                                       @RequestParam(required=false) final boolean updateView) throws ElementException {
    //Update view coordination.
    if (updateView) {
      return ResponseEntity.ok(this.positionDatabase
          .setViewPosition(immutableViewPosition.key(), immutableViewPosition.yaw(), immutableViewPosition.pitch()));
    }

    //Create new
    return ResponseEntity.ok(this.positionDatabase.setViewPosition(
        immutableViewPosition.key(),
        immutableViewPosition.x(),
        immutableViewPosition.y(),
        immutableViewPosition.z(),
        immutableViewPosition.yaw(),
        immutableViewPosition.pitch()));
  }

  /**
   * Insert an new profile from unique id.
   */
  @GetMapping("/view/{key}")
  @ResponseBody
  public ResponseEntity<IViewPosition> getViewPosition(@PathVariable @NotNull final String key) throws ElementException {
    return ResponseEntity.ok(this.positionDatabase.getViewPosition(key));
  }
}
