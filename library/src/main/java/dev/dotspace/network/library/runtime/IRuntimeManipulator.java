package dev.dotspace.network.library.runtime;

import dev.dotspace.common.response.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IRuntimeManipulator {
  @NotNull Response<IRuntime> createRuntime(@Nullable final String id,
                                            @Nullable final RuntimeType runtimeType);

  @NotNull Response<IRuntime> getRuntime(@Nullable final String id);
}
