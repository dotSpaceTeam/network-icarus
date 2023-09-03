package dev.dotspace.network.library.message.content;

import dev.dotspace.network.library.key.IKey;
import dev.dotspace.network.library.message.IMessage;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Store message under key and local.
 */
public interface IPersistentMessage extends IMessage, IKey {
  /**
   * Locale of message.
   *
   * @return locale as {@link Locale}.
   */
  @NotNull Locale locale();
}
