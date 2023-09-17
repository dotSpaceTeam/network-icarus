package dev.dotspace.network.library.log;

import org.jetbrains.annotations.NotNull;

import java.util.Date;


/**
 * Event on message log.
 */
public interface ILogEvent extends ILogMessage {
  /**
   * Time message was logged.
   *
   * @return timestamp on message log.
   */
  @NotNull Date timestamp();

  /**
   * Client which logged the message.
   *
   * @return id of runtime for log.
   */
  @NotNull String runtimeId();
}