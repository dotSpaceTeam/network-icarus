package dev.dotspace.network.library.runtime;

import org.jetbrains.annotations.NotNull;

/**
 * Runtime info.
 */
public interface IRuntime {
  /**
   * Get type of runner.
   *
   * @return type of runner as {@link RuntimeType}.
   */
  @NotNull RuntimeType type();

  /**
   * Id of node. String of {@link java.util.UUID}.
   *
   * @return id as {@link String}.
   */
  @NotNull String runtimeId();
}
