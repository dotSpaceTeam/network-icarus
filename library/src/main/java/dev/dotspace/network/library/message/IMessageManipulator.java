package dev.dotspace.network.library.message;

import dev.dotspace.common.response.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;


/**
 * Manipulate message trough this class.
 */
public interface IMessageManipulator {
  /**
   * Insert a new message only insert not update. If message is already present ignore call.
   *
   * @param locale  of message.
   * @param key     to find message.
   * @param message content of message itself.
   * @return
   */
  @NotNull Response<IMessage> insertMessage(@Nullable final Locale locale,
                                            @Nullable final String key,
                                            @Nullable final String message);

  /**
   * @param locale
   * @param key
   * @param message
   * @return
   */
  @NotNull Response<IMessage> updateMessage(@Nullable final Locale locale,
                                                      @Nullable final String key,
                                                      @Nullable final String message);

  /**
   * @param locale
   * @param key
   * @return
   */
  @NotNull Response<IMessage> message(@Nullable final Locale locale,
                                                 @Nullable final String key);
}
