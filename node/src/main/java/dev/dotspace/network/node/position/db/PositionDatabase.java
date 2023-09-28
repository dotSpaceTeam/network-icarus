package dev.dotspace.network.node.position.db;

import dev.dotspace.network.library.position.IPosition;
import dev.dotspace.network.library.position.IViewPosition;
import dev.dotspace.network.library.position.ImmutablePosition;
import dev.dotspace.network.library.position.ImmutableViewPosition;
import dev.dotspace.network.node.database.AbstractDatabase;
import dev.dotspace.network.node.exception.ElementNotPresentException;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@Log4j2
public final class PositionDatabase extends AbstractDatabase {
  /**
   * Instance of {@link PositionRepository} with queries.
   */
  @Autowired
  private PositionRepository positionRepository;

  /**
   * Instance of {@link ViewPositionRepository} with queries.
   */
  @Autowired
  private ViewPositionRepository viewPositionRepository;

  public @NotNull IPosition setPosition(@Nullable final String key,
                                        final double x,
                                        final double y,
                                        final double z) {
    //Null check
    Objects.requireNonNull(key);

    return ImmutablePosition.of(this.updatePosition(key, x, y, z));
  }

  public @NotNull IPosition getPosition(@Nullable String key) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(key);

    return ImmutablePosition.of(this.position(key));
  }

  public @NotNull IViewPosition setViewPosition(@Nullable final String key,
                                                final double x,
                                                final double y,
                                                final double z,
                                                final double yaw,
                                                final double pitch) {
    //Null check
    Objects.requireNonNull(key);

    return ImmutableViewPosition.of(
        this.updateViewPosition( /*Embed update position.*/
            this.updatePosition(key, x, y, z), /*Set yaw and pitch*/ yaw, pitch));
  }

  public @NotNull IViewPosition setViewPosition(@Nullable final String key,
                                                final double yaw,
                                                final double pitch) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(key);

    //Update position.
    return ImmutableViewPosition.of(this.updateViewPosition(this.position(key), yaw, pitch));
  }

  public @NotNull IViewPosition getViewPosition(@Nullable final String key) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(key);

    //Get base position.
    final PositionElement positionElement = this.position(key);

    return this.viewPositionRepository
        //Search for view position.
        .findByPosition(positionElement)
        //Map to plain object.
        .map(ImmutableViewPosition::of)
        //Error
        .orElseThrow(() -> new ElementNotPresentException("No view position key="+key+" present."));
  }

  private @NotNull PositionElement position(@NotNull final String key) throws ElementNotPresentException {
    return this.positionRepository
        //Get position of key
        .findByKey(key)
        //Else error.
        .orElseThrow(() -> new ElementNotPresentException(null, "No position key="+key+" present."));
  }

  private @NotNull PositionElement updatePosition(@NotNull final String key,
                                                  final double x,
                                                  final double y,
                                                  final double z) {
    @Nullable final PositionElement positionElement = this.positionRepository
        //Get key of position.
        .findByKey(key)
        //Else null.
        .orElse(null);

    if (positionElement != null) {
      //Updated existent.
      positionElement.x(x).y(y).z(z);

      return this.positionRepository.save(positionElement);
    }

    /*
     * Create new one.
     */
    return this.positionRepository.save(new PositionElement(key, x, y, z));
  }

  private @NotNull ViewPositionElement updateViewPosition(@NotNull final PositionElement positionElement,
                                                          final double yaw,
                                                          final double pitch) {
    @Nullable final ViewPositionElement viewPositionElement = this.viewPositionRepository
        //Get view else null.
        .findByPosition(positionElement)
        .orElse(null);

    /*
     * Updated existent.
     */
    if (viewPositionElement != null) {
      viewPositionElement.yaw(yaw).pitch(pitch);
      return this.viewPositionRepository.save(viewPositionElement);
    }

    /*
     * Create new.
     */
    return this.viewPositionRepository.save(new ViewPositionElement(positionElement, yaw, pitch));
  }
}
