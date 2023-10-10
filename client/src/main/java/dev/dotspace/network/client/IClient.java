package dev.dotspace.network.client;

import dev.dotspace.common.function.ThrowableRunnable;
import dev.dotspace.network.client.message.IMessageRequest;
import dev.dotspace.network.client.position.IPositionRequest;
import dev.dotspace.network.client.profile.IProfileRequest;
import dev.dotspace.network.client.web.ClientState;
import dev.dotspace.network.library.common.StateHandler;
import dev.dotspace.network.library.system.ISystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IClient extends ISystem, StateHandler<ClientState> {

  /**
   * See {@link StateHandler#handle(Object, ThrowableRunnable)}
   *
   * @return instance of {@link IClient}
   */
  @Override
  @NotNull IClient handle(final @Nullable ClientState clientState,
                          final @Nullable ThrowableRunnable runnable);

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
   * Get request instance of message.
   *
   * @return inst ance of {@link IMessageRequest}.
   */
  @NotNull IMessageRequest messageRequest();
}