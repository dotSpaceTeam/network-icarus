package dev.dotspace.network.node.message.text;

import dev.dotspace.network.library.message.content.IContentPlaceholder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface ITextMessage {

  @NotNull String plainText();

  @NotNull <TYPE> ITextMessage replace(@Nullable final String string,
                                       @Nullable final TYPE replace);

  @NotNull Set<IContentPlaceholder<?>> placeholders();

  /**
   * Format and create string from message.
   *
   * @return text formatted.
   */
  @NotNull String formatted();
}
