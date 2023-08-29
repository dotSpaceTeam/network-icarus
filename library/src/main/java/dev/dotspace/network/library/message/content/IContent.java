package dev.dotspace.network.library.message.content;

import org.jetbrains.annotations.NotNull;


public interface IContent {
  /**
   * Key to replace in message.
   * Whole replace pattern.
   *
   * @return key to find and replace.
   */
  @NotNull String replaceKey();

  /**
   * Code of placeholder. Only specific content.
   *
   * @return string of code connected to content.
   */
  @NotNull String code();
}
