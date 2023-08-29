package dev.dotspace.network.library.message.content;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


/**
 * Placeholder (key, value storage).
 * Pattern: {{ PLACEHOLDER:[NAME]:[OPTION] }}
 *
 * @param <TYPE> generic type of placeholder.
 */
public interface IContentPlaceholder<TYPE> extends IContent {
  /**
   * Get content assigned to be replaced.
   *
   * @return content to replace.
   */
  @NotNull Optional<TYPE> replaceContext();

  /**
   * Set content to replace.
   *
   * @param replaceContext to set as replace content.
   * @return instance of placeholders
   */
  @NotNull IContentPlaceholder<TYPE> replaceContext(@Nullable final TYPE replaceContext);
}
