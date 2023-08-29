package dev.dotspace.network.client.status;

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
    try {
      return this
          .responseService()
          .response(() -> this.client().get("/v1/running", ImmutableBooleanState.class));
    } catch (final Exception exception) {
      return this.responseService()
          .response(() -> {
            throw new NullPointerException("");
          });
    }
  }
}
