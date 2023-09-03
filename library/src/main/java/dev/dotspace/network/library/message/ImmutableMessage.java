package dev.dotspace.network.library.message;

import dev.dotspace.common.SpaceStrings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


/**
 * Immutable implementation of {@link IMessage}.
 */
public record ImmutableMessage(@NotNull String message) implements IMessage {
  /**
   * * Convert {@link IMessage} to {@link ImmutableMessage}.
   *
   * @param stringContent to convert.
   * @return instance of {@link IMessage}.
   */
  public static @NotNull IMessage of(@Nullable final IMessage stringContent) {
    //Null check
    Objects.requireNonNull(stringContent);

    return new ImmutableMessage(stringContent.message());
  }

  /**
   * * Convert {@link String} to {@link ImmutableMessage}.
   *
   * @param content to convert to String.
   * @return instance of {@link IMessage}.
   */
  public static @NotNull IMessage of(@Nullable final String content) {
    return new ImmutableMessage(content != null ? content : SpaceStrings.plain());
  }
}
