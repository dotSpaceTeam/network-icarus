package dev.dotspace.network.library.message.old;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;

public record ImmutableMessage(@NotNull String key,
                               @NotNull Locale locale,
                               @NotNull String message) implements IMessage {
  /**
   * Convert {@link IMessage} to {@link ImmutableMessage}.
   *
   * @param message to convert.
   * @return instance of {@link IMessage}.
   */
  public static @NotNull IMessage of(@Nullable final IMessage message) {
    //Null check
    Objects.requireNonNull(message);

    return new ImmutableMessage(message.key(), message.locale(), message.message());
  }
}
