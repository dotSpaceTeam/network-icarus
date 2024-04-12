package dev.dotspace.network.library.log;

import org.jetbrains.annotations.NotNull;


/**
 * Level of log.
 */
public interface ILogLevel {
  /**
   * Name of log level.
   *
   * @return name of level as {@link String}. Using enum name of Log4J for examp.e
   */
  @NotNull String levelName();
}
