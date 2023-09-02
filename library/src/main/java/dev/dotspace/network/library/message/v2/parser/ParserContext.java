package dev.dotspace.network.library.message.v2.parser;

import dev.dotspace.network.library.common.EditiableObject;
import dev.dotspace.network.library.message.v2.placeholder.PlaceholderCollection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;


public interface ParserContext {

  void accept(@NotNull final String tag,
              @NotNull final TextElement textElement,
              @NotNull final String value,
              @Nullable final String option,
              @Nullable final Locale locale,
              @NotNull final EditiableObject<String> text,
              @NotNull final PlaceholderCollection placeholderCollection);

}
