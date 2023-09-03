package dev.dotspace.network.library.message.placeholder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public record ImmutablePlaceholder<TYPE>(@NotNull String replaceText,
                                         @NotNull TYPE content
) implements IPlaceholder<TYPE> {
  /**
   * Convert {@link IPlaceholder} to {@link ImmutablePlaceholder}.
   *
   * @param placeholder to convert.
   * @return instance of {@link IPlaceholder}.
   */
  public static <TYPE> @NotNull IPlaceholder<TYPE> of(@Nullable final IPlaceholder<TYPE> placeholder) {
    //Null check
    Objects.requireNonNull(placeholder);

    return new ImmutablePlaceholder<>(placeholder.replaceText(), placeholder.content());
  }

  /**
   * Convert {@link IPlaceholder} of text and content.
   *
   * @param replaceText to convert.
   * @param content     to set onto replaceText.
   * @return instance of {@link IPlaceholder}.
   */
  public static <TYPE> @NotNull IPlaceholder<TYPE> of(@Nullable final String replaceText,
                                                      @Nullable final TYPE content) {
    //Null check
    Objects.requireNonNull(replaceText);
    Objects.requireNonNull(content);

    return new ImmutablePlaceholder<>(replaceText, content);
  }
}