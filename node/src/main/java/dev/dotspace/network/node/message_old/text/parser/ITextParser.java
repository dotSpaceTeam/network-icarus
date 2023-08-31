package dev.dotspace.network.node.message_old.text.parser;

import dev.dotspace.network.node.message_old.text.ITextMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public interface ITextParser {

  @NotNull ITextMessage parse(@Nullable final String message);

  @NotNull ITextMessage parse(@Nullable final String message,
                              @Nullable final Locale locale);

}
