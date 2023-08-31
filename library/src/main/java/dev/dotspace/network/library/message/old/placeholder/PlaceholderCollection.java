package dev.dotspace.network.library.message.old.placeholder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


/**
 * Map to list placeholders.
 */
public final class PlaceholderCollection {
  /**
   * List of placeholders.
   */
  private final @NotNull List<IPlaceholder<?>> placeholderList;

  public PlaceholderCollection() {
    this.placeholderList = new ArrayList<>();
  }

  public <TYPE> @NotNull PlaceholderCollection replace(@Nullable final String replaceText,
                                                       @Nullable final TYPE content) {
    //Add replace to content.
    this.placeholderList.add(ImmutablePlaceholder.of(replaceText, content));
    return this;
  }
}
