package dev.dotspace.network.library.message.v2.content;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;


/**
 * Implementation for {@link IPersistentMessage}.
 */
public record ImmutablePersistentMessage(@NotNull String key,
                                         @NotNull Locale locale,
                                         @NotNull String message
) implements IPersistentMessage {
  /**
   * Convert {@link IPersistentMessage} to {@link ImmutablePersistentMessage}.
   *
   * @param message to convert.
   * @return instance of {@link IPersistentMessage}.
   */
  public static @NotNull IPersistentMessage of(@Nullable final IPersistentMessage message) {
    //Null check
    Objects.requireNonNull(message);

    return new ImmutablePersistentMessage(message.key(), message.locale(), message.message());
  }
}
