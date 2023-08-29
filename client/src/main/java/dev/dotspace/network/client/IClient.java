package dev.dotspace.network.client;

import dev.dotspace.common.function.ThrowableRunnable;
import dev.dotspace.network.client.position.IPositionRequest;
import dev.dotspace.network.client.profile.IProfileRequest;
import dev.dotspace.network.client.session.ISessionRequest;
import dev.dotspace.network.client.status.IStatusRequest;
import dev.dotspace.network.library.common.StateHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IClient extends StateHandler<ClientState> {

  /**
   * See {@link StateHandler#handle(Object, ThrowableRunnable)}
   *
   * @return instance of {@link IClient}
   */
  @Override
  @NotNull IClient handle(final @Nullable ClientState clientState,
                          final @Nullable ThrowableRunnable runnable);

  /**
   * Get status of api point.
   *
   * @return instance of {@link IStatusRequest}.
   */
  @NotNull IStatusRequest statusRequest();

  /**
   * Get request instance of client.
   *
   * @return instance of {@link IProfileRequest}.
   */
  @NotNull IProfileRequest profileRequest();

  /**
   * Get request instance of client.
   *
   * @return instance of {@link IPositionRequest}.
   */
  @NotNull IPositionRequest positionRequest();

  /**
   * Get request instance of client.
   *
   * @return instance of {@link ISessionRequest}.
   */
  @NotNull ISessionRequest sessionRequest();

  /**
   * Get main thread of client.
   *
   * @return instance of {@link Thread}.
   */
  @NotNull Thread thread();
}