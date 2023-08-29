package dev.dotspace.network.library.common;

import dev.dotspace.common.function.ThrowableRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface StateHandler<OBJECT> {
  /**
   * Method to handle change.
   *
   * @param clientState state to execute runnable if entered.
   * @param runnable    to execute.
   * @return instance of handler.
   */
  @NotNull StateHandler<OBJECT> handle(@Nullable final OBJECT clientState,
                                     @Nullable final ThrowableRunnable runnable);

}
