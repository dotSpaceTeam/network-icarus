package dev.dotspace.network.client.status;

import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.common.response.Response;
import dev.dotspace.network.library.state.ImmutableBooleanState;
import org.jetbrains.annotations.NotNull;

public interface IStatusRequest {
  /**
   * Get state of master node. (Similar to ping.)
   *
   * @return response as object.
   */
  @NotNull Response<ImmutableBooleanState> getState();
}
