package dev.dotspace.network.client.rest.web.request;

import dev.dotspace.common.function.ThrowableConsumer;
import dev.dotspace.network.client.rest.web.response.IRestResponse;
import dev.dotspace.network.client.rest.web.response.ImmutableRestResponse;
import dev.dotspace.network.client.rest.web.response.ResponseState;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;


@Log4j2
@Accessors(fluent=true)
public final class RestRequest<RESPONSE, TYPE> implements IRestRequest<RESPONSE, TYPE> {
  /**
   * Create request from webclient from spring.
   */
  private final @NotNull WebClient webClient;
  private final @NotNull List<ThrowableConsumer<IRestResponse<RESPONSE>>> responseList;

  private final @NotNull String endPoint;
  private final @NotNull HttpMethod httpMethod;
  private final @NotNull Class<RESPONSE> responseClass;
  private final @NotNull MultiValueMap<String, String> parameterMap;

  @Setter
  private @Nullable TYPE body;

  public RestRequest(@NotNull WebClient webClient,
                     @NotNull String endPoint,
                     @NotNull HttpMethod httpMethod,
                     @NotNull Class<RESPONSE> responseClass) {
    this.webClient = webClient;
    this.endPoint = endPoint;
    this.httpMethod = httpMethod;
    this.responseClass = responseClass;

    this.responseList = new ArrayList<>();
    this.parameterMap = new LinkedMultiValueMap<>();
  }

  /**
   * True if {@link ClientResponse} was valid.
   */
  private boolean validResponse(@NotNull final ClientResponse clientResponse) {
    return clientResponse.statusCode().is2xxSuccessful()
        || clientResponse.statusCode().value() == 409;
  }

  @Override
  public @NotNull IRestRequest<RESPONSE, TYPE> handle(@Nullable ThrowableConsumer<IRestResponse<RESPONSE>> response) {
    //Null check
    Objects.requireNonNull(response);

    //Add value to list
    this.responseList.add(response);

    return this;
  }

  @Override
  public @NotNull IRestResponse<RESPONSE> request() {
    log.debug("Creating '{}' request to '{}'.", this.httpMethod, this.endPoint);

    //Time enabled.
    final long start = System.currentTimeMillis();
    final RequestBodySpec request = this.webClient
        .method(this.httpMethod)
        //Url
        .uri(uriBuilder -> {
          uriBuilder.path(this.endPoint);

          //Add params to url
          uriBuilder.queryParams(this.parameterMap);

          //Create url
          return uriBuilder.build();
        })
        .acceptCharset(StandardCharsets.UTF_8);

    //If type is present -> set content of request.
    if (this.body != null) {
      //Insert object will be converted to json.
      request.body(BodyInserters.fromValue(this.body));
    }

    //Store state of client
    final AtomicReference<ResponseState> atomicState = new AtomicReference<>(ResponseState.FAILED);
    //Store time
    final AtomicLong atomicTime = new AtomicLong();

    final RESPONSE response = request.exchangeToMono(clientResponse -> {
          atomicTime.set(System.currentTimeMillis()-start);
          //If valid response
          if (this.validResponse(clientResponse)) {
            //Change response state.
            atomicState.set(ResponseState.SUCCESS);
            //Convert response with class.
            return clientResponse.bodyToMono(responseClass);
          }
          //Otherwise crash request
          return clientResponse.createError();
        })

        //Block until response.
        .block();

    //Create response instance
    final IRestResponse<RESPONSE> restResponse =
        new ImmutableRestResponse<>(response, atomicState.get(), atomicTime.get());

    //Loop trough every handle of response.
    for (final ThrowableConsumer<IRestResponse<RESPONSE>> throwableConsumer : this.responseList) {
      try {
        //Accept content
        throwableConsumer.accept(restResponse);
      } catch (final Throwable throwable) {
        //Error message
        log.error("Error while handling rest response. ({})", throwable.getMessage());
      }
    }

    //Return value.
    return restResponse;
  }

}
