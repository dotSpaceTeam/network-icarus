package dev.dotspace.network.node.position.db;

import dev.dotspace.common.SpaceLibrary;
import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.network.library.position.*;
import dev.dotspace.network.node.database.AbstractDatabase;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("positionDatabase")
@Log4j2
public final class PositionDatabase extends AbstractDatabase implements IPositionManipulator {

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

  /**
   * See {@link IPositionManipulator#setPosition(String, long, long, long)}.
   */
  @Override
  public @NotNull CompletableResponse<IPosition> setPosition(@Nullable String key,
                                                             long x,
                                                             long y,
                                                             long z) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(key);

      return ImmutablePosition.of(this.createPosition(key, x, y, z));
    });
  }

  /**
   * See {@link IPositionManipulator#setViewPosition(String, long, long)}.
   */
  @Override
  public @NotNull CompletableResponse<IPosition> getPosition(@Nullable String key) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(key);

      return ImmutablePosition.of(this.positionRepository.findByKey(key).orElse(null));
    });
  }

  @Override
  public @NotNull CompletableResponse<IViewPosition> setViewPosition(@Nullable String key,
                                                                     long x,
                                                                     long y,
                                                                     long z,
                                                                     long yaw,
                                                                     long pitch) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(key);

      return ImmutableViewPosition.of(this.createViewPosition(this.createPosition(key, x, y, z), yaw, pitch));
    });
  }

  /**
   * See {@link IPositionManipulator#setViewPosition(String, long, long)}.
   */
  @Override
  public @NotNull CompletableResponse<IViewPosition> setViewPosition(@Nullable String key,
                                                                     long yaw,
                                                                     long pitch) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(key);

      final PositionElement positionElement = this.positionRepository
        .findByKey(key)
        .orElseThrow(this.failOptional("No key='%s' present, can't find position.".formatted(key)));

      return ImmutableViewPosition.of(this.createViewPosition(positionElement, yaw, pitch));
    });
  }

  /**
   * See {@link IPositionManipulator#getViewPosition(String)}.
   */
  @Override
  public @NotNull CompletableResponse<IViewPosition> getViewPosition(@Nullable String key) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(key);

      final PositionElement positionElement = this.positionRepository.findByKey(key)
        .orElseThrow(this.failOptional("No key='%s' present, can't find base position.".formatted(key)));


      return ImmutableViewPosition.of(this.viewPositionRepository.findByPosition(positionElement).orElse(null));
    });
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
