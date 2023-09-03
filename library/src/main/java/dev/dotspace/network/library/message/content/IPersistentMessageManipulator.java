package dev.dotspace.network.library.message.content;

import dev.dotspace.common.response.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;


/**
 * Manipulate message trough this class.
 */
public interface IPersistentMessageManipulator {
  /**
   * Insert a new message only insert not update. If message is already present ignore call.
   *
   * @param locale  of message.
   * @param key     to find message.
   * @param message content of message itself.
   * @return instance of message.
   * @throws NullPointerException if one parameter is null.
   */
  @NotNull Response<IPersistentMessage> insertMessage(@Nullable final Locale locale,
                                                      @Nullable final String key,
                                                      @Nullable final String message);

  /**
   * Update a present message. If not present insert first.
   *
   * @param locale  of message.
   * @param key     to find message.
   * @param message content of message itself.
   * @return instance of message.
   * @throws NullPointerException if one parameter is null.
   */
  @NotNull Response<IPersistentMessage> updateMessage(@Nullable final Locale locale,
                                                      @Nullable final String key,
                                                      @Nullable final String message);

  /**
   * Get message.
   *
   * @param locale of message.
   * @param key    to find message.
   * @return instance of message, if not present -> {@link Response} absent.
   * @throws NullPointerException if one parameter is null.
   */
  @NotNull Response<IPersistentMessage> message(@Nullable final Locale locale,
                                                @Nullable final String key);
}
