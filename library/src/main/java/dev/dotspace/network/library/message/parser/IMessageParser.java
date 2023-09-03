package dev.dotspace.network.library.message.parser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IMessageParser {

  @NotNull IMessageParser handle(@Nullable final String content,
                                 @Nullable final MatcherConsumer consumer);

  void parse(@Nullable final String text);

}
