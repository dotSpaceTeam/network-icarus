package dev.dotspace.network.library.message;

import dev.dotspace.common.response.CompletableResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public interface IMessageManipulator {
  @NotNull CompletableResponse<Boolean> insertMessage(@Nullable final Locale locale,
                                                      @Nullable final String key,
                                                      @Nullable final String message);

  @NotNull CompletableResponse<Boolean> updateMessage(@Nullable final Locale locale,
                                                      @Nullable final String key,
                                                      @Nullable final String message);

  @NotNull CompletableResponse<IMessage> message(@Nullable final Locale locale,
                                                 @Nullable final String key);
}
