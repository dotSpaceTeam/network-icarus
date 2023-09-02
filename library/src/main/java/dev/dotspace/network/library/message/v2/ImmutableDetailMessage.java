package dev.dotspace.network.library.message.v2;

import dev.dotspace.network.library.message.v2.placeholder.PlaceholderCollection;
import org.jetbrains.annotations.NotNull;


public record ImmutableDetailMessage(@NotNull String plain,
                                     @NotNull String formatted,
                                     @NotNull PlaceholderCollection placeholderCollection
) implements IDetailMessage {
  /**
   * Get message without parsing.
   *
   * @return plain message.
   */
  @Override
  public @NotNull IMessage plainMessage() {
    return ImmutableMessage.of(this.plain);
  }

  /**
   * Get result of parse.
   *
   * @return message.
   */
  @Override
  public @NotNull IMessage formattedMessage() {
    return ImmutableMessage.of(this.formatted);
  }
}
