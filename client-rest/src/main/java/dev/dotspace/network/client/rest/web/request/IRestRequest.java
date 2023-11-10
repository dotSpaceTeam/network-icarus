package dev.dotspace.network.client.rest.web.request;

import dev.dotspace.common.function.ThrowableConsumer;
import dev.dotspace.common.response.Response;
import dev.dotspace.network.client.rest.web.response.IRestResponse;
import dev.dotspace.network.client.rest.web.response.ImmutableRestResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IRestRequest<RESPONSE, TYPE> {
  /**
   * Hook handle to use state of client.
   *
   * @param response to pass to handle.
   * @return class instance.
   */
  @NotNull IRestRequest<RESPONSE, TYPE> handle(@Nullable final ThrowableConsumer<IRestResponse<RESPONSE>> response);

  @NotNull IRestRequest<RESPONSE, TYPE> body(@Nullable final TYPE body);

  @NotNull IRestResponse<RESPONSE> request();

}
