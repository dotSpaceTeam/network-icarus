package dev.dotspace.network.library.profile.storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public record ImmutableProfileStorage(@NotNull String table,
                                      @NotNull String key,
                                      @NotNull String value
) implements IProfileStorage {
  /**
   * Convert {@link IProfileStorage} to {@link ImmutableProfileStorage}.
   *
   * @param profileStorage to convert.
   * @return instance of {@link ImmutableProfileStorage}.
   */
  public static @NotNull IProfileStorage of(@Nullable final IProfileStorage profileStorage) {
    //Null check
    Objects.requireNonNull(profileStorage);

    return new ImmutableProfileStorage(profileStorage.table(), profileStorage.key(), profileStorage.value());
  }
}
