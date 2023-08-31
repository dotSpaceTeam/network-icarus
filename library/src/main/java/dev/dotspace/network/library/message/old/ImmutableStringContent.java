package dev.dotspace.network.library.message.old;

import dev.dotspace.common.SpaceStrings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


/**
 * Immutable implementation of {@link IMessageContent}.
 */
public record ImmutableStringContent(@NotNull String message) implements IMessageContent {
  /**
   * * Convert {@link IMessageContent} to {@link ImmutableStringContent}.
   *
   * @param stringContent to convert.
   * @return instance of {@link IMessageContent}.
   */
  public static @NotNull IMessageContent of(@Nullable final IMessageContent stringContent) {
    //Null check
    Objects.requireNonNull(stringContent);

    return new ImmutableStringContent(stringContent.message());
  }

  /**
   * * Convert {@link String} to {@link ImmutableStringContent}.
   *
   * @param content to convert to String.
   * @return instance of {@link IMessageContent}.
   */
  public static @NotNull IMessageContent of(@Nullable final String content) {
    return new ImmutableStringContent(content != null ? content : SpaceStrings.plain());
  }
}
