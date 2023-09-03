package dev.dotspace.network.library.message.parser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public record MatcherContext(@NotNull String tag,
                             @NotNull String typeField,
                             @NotNull String valueField,
                             @Nullable String optionField
) {
  /**
   * @param text
   * @param content
   * @return
   */
  public @NotNull String replaceFirst(@NotNull String text,
                                      @NotNull String content) {
    return text.replaceFirst(this.tag.replace("{", "\\{"), content);
  }

  /**
   * @param text
   * @param content
   * @return
   */
  public String replace(@NotNull String text,
                        @NotNull String content) {
    return text.replace(this.tag, content);
  }
}
