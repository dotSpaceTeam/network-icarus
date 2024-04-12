package dev.dotspace.network.library.message;

import dev.dotspace.common.function.ThrowableSupplier;
import dev.dotspace.common.response.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IMessageComponent<COMPONENT> {
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
  @NotNull <REPLACE_TYPE> IMessageComponent<COMPONENT> replace(@Nullable final String replaceText,
                                                               @Nullable final ThrowableSupplier<REPLACE_TYPE> content);

  /**
   * Replace content in message.
   *
   * @param replaceText    to set on content on.
   * @param content        to replace position of replaceText.
   * @param <REPLACE_TYPE> generic type of content. Will be converted with {@link String#valueOf(Object)}.
   * @return instance of component.
   */
  @NotNull <REPLACE_TYPE> IMessageComponent<COMPONENT> replace(@Nullable final String replaceText,
                                                               @Nullable final REPLACE_TYPE content);

  @NotNull Response<COMPONENT> complete();
}
