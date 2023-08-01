package dev.dotspace.network.client.status;

import dev.dotspace.common.SpaceLibrary;
import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.network.client.web.AbstractRequest;
import dev.dotspace.network.client.web.IRestClient;
import dev.dotspace.network.library.state.ImmutableBooleanState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class StatusRequest extends AbstractRequest implements IStatusRequest {
  /**
   * See {@link AbstractRequest#AbstractRequest(IRestClient)}
   */
  public StatusRequest(IRestClient client) {
    super(client);
  }

  /**
   * See {@link IStatusRequest#getState()}
   */
  @Override
  public @NotNull CompletableResponse<ImmutableBooleanState> getState() {
    return SpaceLibrary
      .completeResponseAsync(() -> this.client().get("/v1/running", ImmutableBooleanState.class));
  }
}