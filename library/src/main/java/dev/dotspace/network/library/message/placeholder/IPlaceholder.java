package dev.dotspace.network.library.message.placeholder;

import org.jetbrains.annotations.NotNull;


/**
 * Replace placeholder content.
 *
 * @param <TYPE>
 */
public interface IPlaceholder<TYPE> {
  /**
   * Text to replace.
   *
   * @return content to be replaced with {@link IPlaceholder#content()}.
   */
  @NotNull String replaceText();

  /**
   * Get content assigned to be replaced.
   *
   * @return content to replace.
   */
  @NotNull TYPE content();
}
