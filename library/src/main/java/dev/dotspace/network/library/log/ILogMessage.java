package dev.dotspace.network.library.log;

import org.jetbrains.annotations.NotNull;


/**
 * Log message an execute information.
 */
public interface ILogMessage extends ILogLevel {
  /**
   * Class that executed log message.
   *
   * @return execute class, wich logged information.
   */
  @NotNull String eventClass();

  /**
   * Message of log.
   *
   * @return message issued for log.
   */
  @NotNull String message();
}
