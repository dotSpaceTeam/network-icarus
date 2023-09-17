package dev.dotspace.network.client.web;

import dev.dotspace.network.client.ClientState;
import dev.dotspace.network.client.monitoring.ClientMonitoring;
import dev.dotspace.network.library.field.RequestField;
import io.netty.channel.ChannelOption;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;


@Accessors(fluent=true)
@Log4j2
public final class RestClient implements IRestClient {
  /**
   * Monitoring of client.
   */
  @Getter
  @Setter
  private @Nullable ClientMonitoring clientMonitoring;

  /**
   * Spring webclient for request.
   */
  private final @NotNull WebClient webClient;

  public RestClient(@Nullable final String clientId,
                    @Nullable final String service,
                    @Nullable final Duration timeoutDuration) {
    //Null check
    Objects.requireNonNull(clientId);
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
        //Use client as default header
        .defaultHeader(RequestField.CLIENT_ID, clientId)
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .build();

    log.info("Successfully created client to '{}'.", service);
  }

  public RestClient(@Nullable final String clientId) {
    this(clientId, "http://localhost:8443", Duration.ofSeconds(5));

    log.info("Created default(test) client.");
  }

  public RestClient(@Nullable final String clientId,
                    @Nullable final String service) {
    this(clientId, service, Duration.ofSeconds(5));
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

    log.debug("Creating '{}' request to '{}'.", httpMethod, apiEndpoint);

    /*
     * Create request.
     */
    final WebClient.UriSpec<WebClient.RequestBodySpec> requestBodySpec = this
        .webClient
        .method(httpMethod);


    final WebClient.RequestBodySpec request = requestBodySpec
        //Url
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
            //If client monitoring is active reset.
            if (this.clientMonitoring != null) {
              this.clientMonitoring.lastState(ClientState.ESTABLISHED);
            }
            return clientResponse.bodyToMono(typeClass);
          }
          //If client monitoring is active error.
          if (this.clientMonitoring != null) {
            this.clientMonitoring.lastState(ClientState.FAILED);
          }
          return clientResponse.createError();
        })

        /*
         * Block thread until response.
         */
        .block();
  }
}
