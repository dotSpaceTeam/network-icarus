package dev.dotspace.network.client.rest.web;

import dev.dotspace.network.library.common.StateHandler;
import org.jetbrains.annotations.NotNull;


public interface IWebRestClient extends IRequest, StateHandler<ClientState> {
  /**
   * Ping endpoint of client.
   *
   * @return ping to endpoint
   */
  @NotNull Long ping();

  @NotNull ClientState state();

  @NotNull IWebRestClient shutdown();

  boolean active();
}
