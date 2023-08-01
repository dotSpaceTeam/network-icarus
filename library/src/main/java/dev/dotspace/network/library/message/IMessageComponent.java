package dev.dotspace.network.library.message;

import org.jetbrains.annotations.NotNull;

public interface IMessageComponent {
  /**
   * String message to use directly as message.
   *
   * @return String message.
   */
  @NotNull String message();
}
