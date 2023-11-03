package dev.dotspace.network.library.game.message.context;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public final class MessageContext extends AbstractMessageContext {

  public MessageContext(@Nullable ContextType contextType,
                        @Nullable String content,
                        @Nullable Locale locale) {
    super(contextType, content, locale);
  }

  //static
  public static @NotNull MessageContext offline(@Nullable final String message) {
    return new MessageContext(ContextType.OFFLINE, message, Locale.getDefault());
  }

  public static @NotNull MessageContext online(@Nullable final String message,
                                               @Nullable final Locale locale) {
    return new MessageContext(ContextType.ONLINE, message, locale);
  }

  public static @NotNull MessageContext key(@Nullable final String key,
                                            @Nullable final Locale locale) {
    return new MessageContext(ContextType.KEY, key, locale);
  }
}
