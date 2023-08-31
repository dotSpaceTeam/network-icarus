package dev.dotspace.network.library.message.old;

import org.jetbrains.annotations.NotNull;

/**
 * Wrap for string messsage.
 */
public interface IMessageContent {
  /**
   * String message to use directly as message.
   *
   * @return String message.
   */
  @NotNull String message();
}
