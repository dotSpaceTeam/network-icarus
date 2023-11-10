package dev.dotspace.network.client.rest.web;

import dev.dotspace.network.client.rest.web.request.IRestRequest;
import dev.dotspace.network.client.rest.web.response.ResponseState;
import dev.dotspace.network.library.common.StateHandler;
import org.jetbrains.annotations.NotNull;


public interface IWebRestClient extends IRestRequest, StateHandler<ResponseState> {
  /**
   * Ping endpoint of client.
   *
   * @return ping to endpoint
   */
  @NotNull Long ping();

  @NotNull ResponseState state();

  @NotNull IWebRestClient shutdown();

  boolean active();
}
