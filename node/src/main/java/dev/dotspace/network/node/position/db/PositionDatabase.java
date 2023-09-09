package dev.dotspace.network.node.position.db;

import dev.dotspace.network.library.position.IPosition;
import dev.dotspace.network.library.position.IPositionManipulator;
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


@Component("positionDatabase")
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

  public @NotNull IPosition setPosition(@Nullable String key,
                                        long x,
                                        long y,
                                        long z) {
    //Null check
    Objects.requireNonNull(key);

    return ImmutablePosition.of(this.createPosition(key, x, y, z));
  }

  public @NotNull IPosition getPosition(@Nullable String key) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(key);

    return ImmutablePosition.of(
        this.positionRepository
            .findByKey(key)
            .orElseThrow(() -> new ElementNotPresentException(null, "No position key="+key+" found.")));
  }

  public @NotNull IViewPosition setViewPosition(@Nullable String key,
                                                          long x,
                                                          long y,
                                                          long z,
                                                          long yaw,
                                                          long pitch) {
    //Null check
    Objects.requireNonNull(key);

    return ImmutableViewPosition.of(this.createViewPosition(this.createPosition(key, x, y, z), yaw, pitch));
  }

  /**
   * See {@link IPositionManipulator#setViewPosition(String, long, long)}.
   */
  public @NotNull IViewPosition setViewPosition(@Nullable String key,
                                                long yaw,
                                                long pitch) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(key);

    final PositionElement positionElement = this.positionRepository
        .findByKey(key)
        .orElseThrow(() -> new ElementNotPresentException(null, "No key="+key+" present, can't find position."));

    return ImmutableViewPosition.of(this.createViewPosition(positionElement, yaw, pitch));
  }

  /**
   * See {@link IPositionManipulator#getViewPosition(String)}.
   */
  public @NotNull IViewPosition getViewPosition(@Nullable String key) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(key);

    final PositionElement positionElement = this.positionRepository
        .findByKey(key)
        .orElseThrow(() -> new ElementNotPresentException(null, "No key="+key+" present, can't find base position."));


    return ImmutableViewPosition.of(this.viewPositionRepository.findByPosition(positionElement).orElse(null));
  }

  private @NotNull PositionElement createPosition(@NotNull final String key,
                                                  final long x,
                                                  final long y,
                                                  final long z) {
    @Nullable final PositionElement positionElement = this.positionRepository.findByKey(key).orElse(null);

    if (positionElement != null) {
      /*
       * Updated existent.
       */
      positionElement.x(x).y(y).z(z);
      return this.positionRepository.save(positionElement);
    }

    /*
     * Create new one.
     */
    return this.positionRepository.save(new PositionElement(key, x, y, z));
  }

  private @NotNull ViewPositionElement createViewPosition(@NotNull final PositionElement positionElement,
                                                          long yaw,
                                                          long pitch) {
    @Nullable final ViewPositionElement viewPositionElement = this.viewPositionRepository
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
