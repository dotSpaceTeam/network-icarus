package dev.dotspace.network.client.rest.position;

import dev.dotspace.common.response.Response;
import dev.dotspace.network.client.rest.web.request.AbstractRequest;
import dev.dotspace.network.library.position.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public final class PositionRequest extends AbstractRequest implements IPositionRequest {
  /**
   * See {@link IPositionRequest#setPosition(String, long, long, long)}.
   */
  @Override
  public @NotNull Response<IPosition> setPosition(@Nullable String key,
                                                  long x,
                                                  long y,
                                                  long z) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(key);

      return this.client().put("/v1/position", ImmutablePosition.class, new ImmutablePosition(key, x, y, z));
    });
  }

  /**
   * See {@link IPositionRequest#getPosition(String)}.
   */
  @Override
  public @NotNull Response<IPosition> getPosition(@Nullable String key) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(key);

      return this.client().get("/v1/position/"+key, ImmutablePosition.class);
    });
  }

  /**
   * See {@link IPositionRequest#setViewPosition(String, long, long, long, long, long)}.
   */
  @Override
  public @NotNull Response<IViewPosition> setViewPosition(@Nullable String key,
                                                          long x,
                                                          long y,
                                                          long z,
                                                          long yaw,
                                                          long pitch) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(key);

      return this.client().put("/v1/position/view",
          ImmutableViewPosition.class, new ImmutableViewPosition(key, x, y, z, yaw, pitch));
    });
  }

  /**
   * See {@link IPositionRequest#getViewPosition(String)}.
   */
  @Override
  public @NotNull Response<IViewPosition> getViewPosition(@Nullable String key) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(key);

      return this.client().get("/v1/position/view/"+key, ImmutableViewPosition.class);
    });
  }
}
