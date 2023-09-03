package dev.dotspace.network.library.message;

import dev.dotspace.common.function.ThrowableSupplier;
import dev.dotspace.common.response.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IMessageComponent extends IMessage {
  /**
   * Replace content in message.
   * Information of content will be pulled on message update. (Good for 'real time applications.')
   * Or simple multiple lines.
   *
   * @param replaceText    to set on content on.
   * @param content        to replace position of replaceText.
   * @param <REPLACE_TYPE> generic type of content. Will be converted with {@link String#valueOf(Object)}.
   * @return instance of component.
   */
  @NotNull <REPLACE_TYPE> IMessageComponent replace(@Nullable final String replaceText,
                                                    @Nullable final ThrowableSupplier<REPLACE_TYPE> content);

  /**
   * Replace content in message.
   *
   * @param replaceText    to set on content on.
   * @param content        to replace position of replaceText.
   * @param <REPLACE_TYPE> generic type of content. Will be converted with {@link String#valueOf(Object)}.
   * @return instance of component.
   */
  @NotNull <REPLACE_TYPE> IMessageComponent replace(@Nullable final String replaceText,
                                                    @Nullable final REPLACE_TYPE content);

  /**
   * Convert message to string.
   */
  @NotNull String convert();

  @NotNull <TYPE> TYPE convert(@Nullable final Class<TYPE> typeClass);

  @NotNull <TYPE> Response<TYPE> convertAsync(@Nullable final Class<TYPE> typeClass);
}
