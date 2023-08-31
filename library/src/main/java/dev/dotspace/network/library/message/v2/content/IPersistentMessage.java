package dev.dotspace.network.library.message.v2.content;

import dev.dotspace.network.library.message.v2.IMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Store message under key and local.
 */
public interface IPersistentMessage extends IMessage {
  /**
   * Key of message.
   *
   * @return key as {@link String}.
   */
  @NotNull String key();

  /**
   * Locale of message.
   *
   * @return locale as {@link Locale}.
   */
  @NotNull Locale locale();
}
