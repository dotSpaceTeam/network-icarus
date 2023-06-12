package dev.dotspace.network.node.message.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface ITextMessage {

  @NotNull String plainText();

  @NotNull <TYPE> ITextMessage replace(@Nullable final String string,
                                       @Nullable final TYPE replace);

  @NotNull <TYPE> ITextMessage replace(@Nullable final String string,
                                       @Nullable final PlaceholderContext<@Nullable TYPE> replaceContext);

  @NotNull Set<IPlaceholder<?>> placeholders();

  /**
   * Format and create string from message.
   *
   * @return
   */
  @NotNull String formatted();
}
