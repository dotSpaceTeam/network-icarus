package dev.dotspace.network.client.web;

import dev.dotspace.network.library.common.StateHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IRestClient extends StateHandler<ClientState> {
  /**
   * Get component from endpoint.
   *
   * @param apiEndpoint to connect service.
   * @param typeClass   to build object from.
   * @param <RESPONSE>  generic type of response.
   * @return response object.
   */
  <RESPONSE> @NotNull RESPONSE get(@Nullable final String apiEndpoint,
                                   @Nullable final Class<RESPONSE> typeClass);

  <RESPONSE, TYPE> @NotNull RESPONSE put(@Nullable final String apiEndpoint,
                                         @Nullable final Class<RESPONSE> typeClass,
                                         @Nullable final TYPE type);

  <RESPONSE, TYPE> @NotNull RESPONSE post(@Nullable final String apiEndpoint,
                                          @Nullable final Class<RESPONSE> typeClass,
                                          @Nullable final TYPE type);

  <RESPONSE, TYPE> @NotNull RESPONSE delete(@Nullable final String apiEndpoint,
                                            @Nullable final Class<RESPONSE> typeClass,
                                            @Nullable final TYPE type);

  /**
   * Ping endpoint of client.
   *
   * @return ping to endpoint
   */
  @NotNull Long ping();

  @NotNull ClientState state();
}
