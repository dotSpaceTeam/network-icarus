package dev.dotspace.network.client.web;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IRestClient {
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
}
