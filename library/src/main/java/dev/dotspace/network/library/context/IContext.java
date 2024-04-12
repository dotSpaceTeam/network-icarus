package dev.dotspace.network.library.context;

import dev.dotspace.common.function.ThrowableConsumer;
import dev.dotspace.common.response.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Context for connection based operations.
 *
 * @param <TYPE> generic type of object.
 */
public interface IContext<TYPE> {
  /**
   * Get type and block current thread.
   *
   * @return object.
   */
  @NotNull TYPE forceComplete();

  /**
   * Get type as async completed response.
   */
  @NotNull Response<TYPE> complete();

  /**
   * Handle state of completed.
   */
  @NotNull IContext<TYPE> handle(@Nullable final ThrowableConsumer<TYPE> handleConsumer);

}
