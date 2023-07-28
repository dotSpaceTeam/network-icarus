package dev.dotspace.network.node.database;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

@Log4j2
public abstract class AbstractDatabase {
  /**
   * Used as the {@link Optional#orElseThrow(Supplier)} supplier with {@link NullPointerException}.
   *
   * @param message to set as error message.
   * @return supplier with error.
   */
  protected Supplier<NullPointerException> failOptional(@NotNull final String message) {
    return () -> {
      //Error message for logger.
      log.error(message);
      //Exception to fail optional.
      return new NullPointerException(message);
    };
  }

}
