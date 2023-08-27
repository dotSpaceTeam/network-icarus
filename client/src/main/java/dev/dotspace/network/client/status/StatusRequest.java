package dev.dotspace.network.client.status;

import dev.dotspace.common.SpaceLibrary;
import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.common.response.Response;
import dev.dotspace.network.client.web.AbstractRequest;
import dev.dotspace.network.library.state.ImmutableBooleanState;
import org.jetbrains.annotations.NotNull;


public final class StatusRequest extends AbstractRequest implements IStatusRequest {
  /**
   * See {@link IStatusRequest#getState()}
   */
  @Override
  public @NotNull Response<ImmutableBooleanState> getState() {
    return this
        .responseService()
        .response(() -> {
          return this.client().get("/v1/running", ImmutableBooleanState.class);
        });
  }
}
