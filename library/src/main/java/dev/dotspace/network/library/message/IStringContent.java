package dev.dotspace.network.library.message;

import org.jetbrains.annotations.NotNull;

/**
 * Wrap for string messsage.
 */
public interface IStringContent {
  /**
   * String message to use directly as message.
   *
   * @return String message.
   */
  @NotNull String message();
}
