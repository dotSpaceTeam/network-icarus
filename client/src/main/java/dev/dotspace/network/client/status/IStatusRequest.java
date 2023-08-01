package dev.dotspace.network.client.status;

import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.network.library.state.ImmutableBooleanState;
import org.jetbrains.annotations.NotNull;

public interface IStatusRequest {

  @NotNull CompletableResponse<ImmutableBooleanState> getState();
}