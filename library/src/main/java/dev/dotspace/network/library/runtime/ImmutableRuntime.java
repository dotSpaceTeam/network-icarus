package dev.dotspace.network.library.runtime;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record ImmutableRuntime(@NotNull RuntimeType type,
                               @NotNull String runtimeId) implements IRuntime {
  /**
   * Convert {@link IRuntime} to {@link ImmutableRuntime}.
   *
   * @param runtime to convert.
   * @return instance of {@link IRuntime}.
   */
  public static @NotNull IRuntime of(@Nullable final IRuntime runtime) {
    //Null check
    Objects.requireNonNull(runtime);

    return new ImmutableRuntime(runtime.type(), runtime.runtimeId());
  }
}
