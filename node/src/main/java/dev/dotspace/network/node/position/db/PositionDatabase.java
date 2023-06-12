package dev.dotspace.network.node.position.db;

import dev.dotspace.common.SpaceLibrary;
import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.network.library.position.IPosition;
import dev.dotspace.network.library.position.IViewPosition;
import dev.dotspace.network.library.position.ImmutablePosition;
import dev.dotspace.network.library.position.ImmutableViewPosition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("positionDatabase")
public final class PositionDatabase implements IPositionDatabase {
  /**
   * Logger
   */
  private final static @NotNull Logger LOGGER = LogManager.getLogger(PositionDatabase.class);

  @Autowired
  private PositionRepository positionRepository;

  @Autowired
  private ViewPositionRepository viewPositionRepository;

  /**
   * See {@link IPositionDatabase#setPosition(String, long, long, long)}.
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
   * See {@link IPositionDatabase#setViewPosition(String, long, long)}.
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

      return ImmutableViewPosition.of(this.createViewPosition( this.createPosition(key, x, y, z), yaw,pitch));
    });
  }

  /**
   * See {@link IPositionDatabase#setViewPosition(String, long, long)}.
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
        .orElseThrow(() -> {
          LOGGER.error("No key='{}' present, can't find position.", key);
          return new NullPointerException();
        });

      return ImmutableViewPosition.of(this.createViewPosition(positionElement, yaw, pitch));
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
