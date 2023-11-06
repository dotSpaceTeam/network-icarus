package dev.dotspace.network.client.rest;

import dev.dotspace.common.function.ThrowableRunnable;
import dev.dotspace.network.client.rest.message.IMessageRequest;
import dev.dotspace.network.client.rest.position.IPositionRequest;
import dev.dotspace.network.client.rest.profile.IProfileRequest;
import dev.dotspace.network.client.rest.web.ClientState;
import dev.dotspace.network.library.common.StateHandler;
import dev.dotspace.network.library.system.participant.ISystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IRestClient extends ISystem, StateHandler<ClientState> {

  /**
   * See {@link StateHandler#handle(Object, ThrowableRunnable)}
   *
   * @return instance of {@link IRestClient}
   */
  @Override
  @NotNull IRestClient handle(final @Nullable ClientState clientState,
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