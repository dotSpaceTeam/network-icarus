package dev.dotspace.network.client.rest.message;

import dev.dotspace.common.response.Response;
import dev.dotspace.network.library.message.IMessage;
import dev.dotspace.network.library.message.content.IPersistentMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;


public interface IMessageRequest {
  /**
   * Send message to server and get formatted back.
   *
   * @param message
   * @return
   */
  @NotNull Response<IMessage> formatString(@Nullable final String message);

  /**
   * Get message from locale and key.
   *
   * @param locale
   * @param key
   * @return
   */
  @NotNull Response<IMessage> getMessage(@Nullable final Locale locale,
                                         @Nullable final String key);

  /**
   * Just create no update.
   *
   * @param locale
   * @param key
   * @param message
   * @return
   */
  @NotNull Response<IPersistentMessage> updateMessage(@Nullable final Locale locale,
                                                      @Nullable final String key,
                                                      @Nullable final String message,
                                                      final boolean createOnly);
}
