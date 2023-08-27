package dev.dotspace.network.library.message;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public interface IMessage extends IMessageComponent {
  /**
   * Key of message.
   *
   * @return key as {@link String}.
   */
  @NotNull String key();

  /**
   * Locale of message. The locale message of {@link IMessageComponent#message()}.
   *
   * @return locale as {@link Locale}.
   */
  @NotNull Locale locale();
}
