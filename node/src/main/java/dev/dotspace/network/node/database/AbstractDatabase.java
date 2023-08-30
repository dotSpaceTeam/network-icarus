package dev.dotspace.network.node.database;

import dev.dotspace.common.response.ResponseService;
import dev.dotspace.network.node.SpringConfig;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.function.Supplier;


@Log4j2
@Accessors(fluent=true)
public abstract class AbstractDatabase {
  /**
   * Instance of service of spring configure {@link SpringConfig#responseService()}.
   */
  @Getter
  @Autowired
  private ResponseService responseService;

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
