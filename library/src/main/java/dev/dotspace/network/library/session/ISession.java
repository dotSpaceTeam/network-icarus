package dev.dotspace.network.library.session;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

/**
 * Session info.
 */
public interface ISession extends IPlaytime {
  /**
   * Session id.
   */
  @NotNull Long sessionId();

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
}
