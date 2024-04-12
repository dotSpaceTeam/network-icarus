package dev.dotspace.network.client.rest;

import dev.dotspace.common.function.ThrowableRunnable;
import dev.dotspace.common.response.Response;
import dev.dotspace.network.client.rest.message.IMessageRequest;
import dev.dotspace.network.client.rest.position.IPositionRequest;
import dev.dotspace.network.client.rest.profile.IProfileRequest;
import dev.dotspace.network.client.rest.web.response.IRestResponse;
import dev.dotspace.network.client.rest.web.response.ResponseState;
import dev.dotspace.network.client.rest.web.IWebRestClient;
import dev.dotspace.network.library.common.StateHandler;
import dev.dotspace.network.library.system.participant.ISystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IRestClient extends ISystem, StateHandler<ResponseState> {
  /**
   * See {@link StateHandler#handle(Object, ThrowableRunnable)}
   *
   * @return instance of {@link IRestClient}
   */
  @Override
  @NotNull IRestClient handle(final @Nullable ResponseState responseState,
                              final @Nullable ThrowableRunnable runnable);

  /**
   * Ping endpoint of client.
   *
   * @return ping to endpoint
   */
  @NotNull Long ping();

  @NotNull ResponseState state();

  @NotNull IWebRestClient shutdown();

  boolean active();

  /**
   * Get component from endpoint.
   *
   * @param apiEndpoint   to connect service.
   * @param responseClass to build object from.
   * @param <RESPONSE>    generic type of response.
   * @return response object.
   */
  <RESPONSE> @NotNull Response<RESPONSE> get(@Nullable final String apiEndpoint,
                                             @Nullable final Class<RESPONSE> responseClass);

  /**
   * Get Request
   */
  <RESPONSE, TYPE> @NotNull Response<RESPONSE> get(@Nullable final String apiEndpoint,
                                                   @Nullable final Class<RESPONSE> responseClass,
                                                   @Nullable final TYPE body);

  /**
   * Put request
   */
  <RESPONSE> @NotNull Response<RESPONSE> put(@Nullable final String apiEndpoint,
                                             @Nullable final Class<RESPONSE> responseClass);


  /**
   * Put request
   */
  <RESPONSE, TYPE> @NotNull Response<RESPONSE> put(@Nullable final String apiEndpoint,
                                                   @Nullable final Class<RESPONSE> responseClass,
                                                   @Nullable final TYPE body);

  <RESPONSE> @NotNull Response<RESPONSE> post(@Nullable final String apiEndpoint,
                                              @Nullable final Class<RESPONSE> responseClass);

  <RESPONSE, TYPE> @NotNull Response<RESPONSE> post(@Nullable final String apiEndpoint,
                                                    @Nullable final Class<RESPONSE> responseClass,
                                                    @Nullable final TYPE body);

  <RESPONSE> @NotNull Response<RESPONSE> delete(@Nullable final String apiEndpoint,
                                                @Nullable final Class<RESPONSE> responseClass);

  <RESPONSE, TYPE> @NotNull Response<RESPONSE> delete(@Nullable final String apiEndpoint,
                                                      @Nullable final Class<RESPONSE> responseClass,
                                                      @Nullable final TYPE body);

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