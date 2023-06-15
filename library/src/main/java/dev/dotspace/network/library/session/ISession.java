package dev.dotspace.network.library.session;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Date;

/**
 * Session info.
 */
public interface ISession {
  /**
   * Time the session has started.
   *
   * @return time as {@link Date}.
   */
  @NotNull Date startDate();

  /**
   * Time the session has ended.
   *
   * @return time as {@link Date}.
   */
  @NotNull Date endDate();

  /**
   * Duration between two values.
   *
   * @return difference as {@link Duration}.
   */
  @NotNull Duration duration();
}
