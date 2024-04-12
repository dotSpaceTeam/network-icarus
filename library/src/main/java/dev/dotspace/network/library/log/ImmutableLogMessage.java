package dev.dotspace.network.library.log;

import dev.dotspace.network.library.economy.ICurrency;
import dev.dotspace.network.library.economy.ImmutableCurrency;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public record ImmutableLogMessage(@NotNull String levelName,
                                  @NotNull String eventClass,
                                  @NotNull String message
) implements ILogMessage {
  /**
   * Convert {@link ILogMessage} to {@link ImmutableLogMessage}.
   *
   * @param message to convert.
   * @return instance of {@link ILogMessage}.
   */
  public static @NotNull ILogMessage of(@Nullable final ILogMessage message) {
    //Null check
    Objects.requireNonNull(message);

    return new ImmutableLogMessage(message.levelName(), message.eventClass(), message.message());
  }
}
