package dev.dotspace.network.library.message;

import dev.dotspace.common.SpaceStrings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


/**
 * Immutable implementation of {@link IStringContent}.
 */
public record ImmutableStringContent(@NotNull String message) implements IStringContent {
  /**
   * * Convert {@link IStringContent} to {@link ImmutableStringContent}.
   *
   * @param stringContent to convert.
   * @return instance of {@link IStringContent}.
   */
  public static @NotNull IStringContent of(@Nullable final IStringContent stringContent) {
    //Null check
    Objects.requireNonNull(stringContent);

    return new ImmutableStringContent(stringContent.message());
  }

  /**
   * * Convert {@link String} to {@link ImmutableStringContent}.
   *
   * @param content to convert to String.
   * @return instance of {@link IStringContent}.
   */
  public static @NotNull IStringContent of(@Nullable final String content) {
    return new ImmutableStringContent(content != null ? content : SpaceStrings.plain());
  }
}
