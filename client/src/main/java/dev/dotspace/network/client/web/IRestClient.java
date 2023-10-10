package dev.dotspace.network.client.web;

import dev.dotspace.network.library.common.StateHandler;
import org.jetbrains.annotations.NotNull;


public interface IRestClient extends IRequest, StateHandler<ClientState> {
  /**
   * Ping endpoint of client.
   *
   * @return ping to endpoint
   */
  @NotNull Long ping();

  @NotNull ClientState state();

  @NotNull IRestClient shutdown();

  boolean active();
}
