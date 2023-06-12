package dev.dotspace.network.node.message.db;

import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.network.library.message.IMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public interface IMessageDatabase {
  @NotNull CompletableResponse<Boolean> insertMessage(@Nullable final Locale locale,
                                                      @Nullable final String key,
                                                      @Nullable final String message);

  @NotNull CompletableResponse<Boolean> updateMessage(@Nullable final Locale locale,
                                                      @Nullable final String key,
                                                      @Nullable final String message);

  @NotNull CompletableResponse<IMessage> message(@Nullable final Locale locale,
                                                 @Nullable final String key);
}
