package dev.dotspace.network.client.web;

import io.netty.channel.ChannelOption;
import lombok.experimental.Accessors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;

@Component
@Accessors(fluent = true)
public final class RestClient implements IRestClient {
  /**
   * Logger.
   */
  private final static Logger LOGGER = LogManager.getLogger(RestClient.class);

  /**
   * Spring webclient for request.
   */
  private final @NotNull org.springframework.web.reactive.function.client.WebClient webClient;

  public RestClient(@Nullable final String service,
                    @Nullable final Duration timeoutDuration) {
    //Null check
    Objects.requireNonNull(service);
    Objects.requireNonNull(timeoutDuration);

    /*
     * Create client instance.
     */
    final HttpClient httpClient = HttpClient.create()
      .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Math.toIntExact(timeoutDuration.toMillis()))
      .responseTimeout(timeoutDuration);

    this.webClient = org.springframework.web.reactive.function.client.WebClient.builder()
      /*
       * Set base url.
       */
      .baseUrl(service)

      /*
       * Default headers.
       */
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .defaultHeader(HttpHeaders.ACCEPT_CHARSET)
      .clientConnector(new ReactorClientHttpConnector(httpClient))
      .build();
    LOGGER.info("Successfully created client to '{}'.", service);
  }

  public RestClient() {
    this("http://localhost:8080", Duration.ofSeconds(5));
    LOGGER.info("Created default(test) client.");
  }

  /**
   * See {@link IRestClient#get(String, Class)}.
   */
  @Override
  public <RESPONSE> @NotNull RESPONSE get(@Nullable String apiEndpoint,
                                          @Nullable Class<RESPONSE> typeClass) {
    return this.response(apiEndpoint, HttpMethod.GET, typeClass, null);
  }

  /**
   * See {@link IRestClient#put(String, Class, Object)}.
   */
  public <RESPONSE, TYPE> @NotNull RESPONSE put(@Nullable String apiEndpoint,
                                                @Nullable Class<RESPONSE> typeClass,
                                                @Nullable TYPE type) {
    return this.response(apiEndpoint, HttpMethod.PUT, typeClass, type);
  }

  /**
   * See {@link IRestClient#post(String, Class, Object)}.
   */
  public <RESPONSE, TYPE> @NotNull RESPONSE post(@Nullable String apiEndpoint,
                                                 @Nullable Class<RESPONSE> typeClass,
                                                 @Nullable TYPE type) {
    return this.response(apiEndpoint, HttpMethod.POST, typeClass, type);
  }

  /**
   * See {@link IRestClient#delete(String, Class, Object)}.
   */
  public <RESPONSE, TYPE> @NotNull RESPONSE delete(@Nullable String apiEndpoint,
                                                   @Nullable Class<RESPONSE> typeClass,
                                                   @Nullable TYPE type) {
    return this.response(apiEndpoint, HttpMethod.POST, typeClass, type);
  }

  private <RESPONSE, TYPE> RESPONSE response(@Nullable final String apiEndpoint,
                                             @Nullable final HttpMethod httpMethod,
                                             @Nullable final Class<RESPONSE> typeClass,
                                             @Nullable final TYPE type) {
    //Null check
    Objects.requireNonNull(apiEndpoint);
    Objects.requireNonNull(httpMethod);
    Objects.requireNonNull(typeClass);

    LOGGER.debug("Creating '{}' request to '{}'.", httpMethod, apiEndpoint);

    /*
     * Create request.
     */
    final org.springframework.web.reactive.function.client.WebClient.UriSpec<org.springframework.web.reactive.function.client.WebClient.RequestBodySpec> requestBodySpec = this
      .webClient
      .method(httpMethod);

    final org.springframework.web.reactive.function.client.WebClient.RequestBodySpec request = requestBodySpec
      .uri(apiEndpoint)
      .acceptCharset(StandardCharsets.UTF_8);

    if (type != null) {
      request
        .body(BodyInserters.fromValue(type));
    }

    return request.exchangeToMono(clientResponse -> {
        /*
         * Only  build object if ok.
         */
        if (clientResponse.statusCode().equals(HttpStatus.OK)) {
          return clientResponse.bodyToMono(typeClass);
        }
        LOGGER.warn("Request error.");
        return clientResponse.createError();
      })

      /*
       * Block thread until response.
       */
      .block();
  }
}