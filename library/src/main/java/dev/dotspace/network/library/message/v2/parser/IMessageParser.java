package dev.dotspace.network.library.message.v2.parser;

import dev.dotspace.network.library.message.v2.IDetailMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;


public interface IMessageParser {

  @NotNull IDetailMessage parse(@Nullable final String message);

  @NotNull IDetailMessage parse(@Nullable final String message,
                                @Nullable final Locale locale);

  @NotNull IMessageParser handle(@Nullable final TextElement textElement,
                                 @Nullable final ParserContext parserContext);
}
