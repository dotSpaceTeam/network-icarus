package dev.dotspace.network.node.position.db;

import dev.dotspace.common.SpaceLibrary;
import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.network.library.position.IPosition;
import dev.dotspace.network.library.position.ImmutablePosition;
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

      final PositionElement positionElement = this.positionRepository.findByKey(key).orElse(null);

      if (positionElement != null) {
        //Set values.
        positionElement.x(x).y(y).z(z);
        return ImmutablePosition.of(this.positionRepository.save(positionElement));
      }

      /*
       * Create new one.
       */
      return ImmutablePosition.of(this.positionRepository.save(new PositionElement(key, x, y, z)));
    });
  }

  @Override
  public @NotNull CompletableResponse<IPosition> getPosition(@Nullable String key) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(key);

      return ImmutablePosition.of(this.positionRepository.findByKey(key).orElse(null));
    });
  }
}
