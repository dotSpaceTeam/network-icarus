package dev.dotspace.network.node.message.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Placeholder (key, value storage).
 * Pattern: {{ PLACEHOLDER:[NAME]:[OPTION] }}
 *
 * @param <TYPE> generic type of placeholder.
 */
public interface IPlaceholder<TYPE> {
  /**
   * Key to replace in message.
   * Whole replace pattern.
   *
   * @return key to find and replace.
   */
  @NotNull String replaceKey();

  @NotNull String code();

  @NotNull Optional<TYPE> replaceContext();

  @NotNull IPlaceholder<TYPE> replaceContext(@Nullable final TYPE replaceContext);
}
