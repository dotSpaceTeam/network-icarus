package dev.dotspace.network.library.game.message.context;

import dev.dotspace.common.function.ThrowableSupplier;
import dev.dotspace.common.response.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IMessageContext {
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
  @NotNull <REPLACE_TYPE> IMessageContext replace(@Nullable final String replaceText,
                                                  @Nullable final ThrowableSupplier<REPLACE_TYPE> content);

  /**
   * Replace content in message.
   *
   * @param replaceText    to set on content on.
   * @param content        to replace position of replaceText.
   * @param <REPLACE_TYPE> generic type of content. Will be converted with {@link String#valueOf(Object)}.
   * @return instance of component.
   */
  @NotNull <REPLACE_TYPE> IMessageContext replace(@Nullable final String replaceText,
                                                  @Nullable final REPLACE_TYPE content);

  /**
   * Cache being ignored while context parse.
   */
  @NotNull IMessageContext ignoreCache();

  /**
   * Type of context.
   * Also see {@link ContextType}.
   */
  @NotNull ContextType contextType();

  @NotNull String complete();

  @NotNull Response<String> completeAsync();
}
