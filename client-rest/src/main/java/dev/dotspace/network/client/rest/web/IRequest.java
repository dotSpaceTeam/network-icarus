package dev.dotspace.network.client.rest.web;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IRequest {
  /**
   * Get component from endpoint.
   *
   * @param apiEndpoint to connect service.
   * @param responseClass   to build object from.
   * @param <RESPONSE>  generic type of response.
   * @return response object.
   */
  <RESPONSE> @NotNull RESPONSE get(@Nullable final String apiEndpoint,
                                   @Nullable final Class<RESPONSE> responseClass);

  /**
   * Get Request
   */
  <RESPONSE, TYPE> @NotNull RESPONSE get(@Nullable final String apiEndpoint,
                                         @Nullable final Class<RESPONSE> responseClass,
                                         @Nullable final TYPE body);

  /**
   * Put request
   */
  <RESPONSE> @NotNull RESPONSE put(@Nullable final String apiEndpoint,
                                   @Nullable final Class<RESPONSE> responseClass);


  /**
   * Put request
   */
  <RESPONSE, TYPE> @NotNull RESPONSE put(@Nullable final String apiEndpoint,
                                         @Nullable final Class<RESPONSE> responseClass,
                                         @Nullable final TYPE body);

  <RESPONSE> @NotNull RESPONSE post(@Nullable final String apiEndpoint,
                                    @Nullable final Class<RESPONSE> responseClass);

  <RESPONSE, TYPE> @NotNull RESPONSE post(@Nullable final String apiEndpoint,
                                          @Nullable final Class<RESPONSE> responseClass,
                                          @Nullable final TYPE body);

  <RESPONSE> @NotNull RESPONSE delete(@Nullable final String apiEndpoint,
                                      @Nullable final Class<RESPONSE> responseClass);

  <RESPONSE, TYPE> @NotNull RESPONSE delete(@Nullable final String apiEndpoint,
                                            @Nullable final Class<RESPONSE> responseClass,
                                            @Nullable final TYPE body);
}
