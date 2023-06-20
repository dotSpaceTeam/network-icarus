package dev.dotspace.network.library.message;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public interface IMessage extends ITextMessage {

  @NotNull String key();

  @NotNull Locale locale();

}
