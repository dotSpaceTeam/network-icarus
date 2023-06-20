package dev.dotspace.network.library.runtime;

import dev.dotspace.common.response.CompletableResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IRuntimeManipulator {
  @NotNull CompletableResponse<IRuntime> createRuntime(@Nullable final String id,
                                                       @Nullable final RuntimeType runtimeType);

  @NotNull CompletableResponse<IRuntime> getRuntime(@Nullable final String id);
}
