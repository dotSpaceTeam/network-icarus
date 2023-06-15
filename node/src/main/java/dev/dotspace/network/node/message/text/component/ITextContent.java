package dev.dotspace.network.node.message.text.component;

import org.jetbrains.annotations.NotNull;

public interface ITextContent {
  /**
   * Get message raw.
   *
   * @return raw text message.
   */
  @NotNull String message();

}
