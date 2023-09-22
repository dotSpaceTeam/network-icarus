package dev.dotspace.network.library.message.content;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;


/**
 * Implementation for {@link IPersistentMessage}.
 */
//Swagger
@Schema(
    name="PersistentMessage",
    description="Store message for language under a specific key."
)
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
