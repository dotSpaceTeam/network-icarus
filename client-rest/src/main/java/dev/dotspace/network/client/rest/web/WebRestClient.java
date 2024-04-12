package dev.dotspace.network.client.rest.web;

import dev.dotspace.common.function.ThrowableRunnable;
import dev.dotspace.network.client.rest.web.response.ResponseState;
import dev.dotspace.network.library.common.StateHandler;
import dev.dotspace.network.library.common.StateMap;
import dev.dotspace.network.library.field.RequestField;
import io.netty.channel.ChannelOption;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;


@Accessors(fluent=true)
@Log4j2
public final class WebRestClient implements IWebRestClient {
  //################## Config values ######################
  /**
   * Amount of stored pings for average ping.
   */
  private final static long DEFAULT_PING_INTERVAL = 10000L;

  /**
   * Spring webclient for request.
   */
  private final @NotNull WebClient webClient;
  /**
   * Amount of pings in total.
   */
  private long totalPingCount;
  /**
   * Last time
   */
  private long lastStateChange;

  private long longestResponseTime;
  @Getter
  private boolean active;

  @Getter
  private @NotNull ResponseState state;
  /**
   * Store values of state runnable.
   */
  private final @NotNull StateMap<ResponseState> stateMap;

  @SuppressWarnings("busy-waiting")
  public WebRestClient(@Nullable final String clientId,
                       @Nullable final String service,
                       @Nullable final Duration timeoutDuration) {
    //Null check
    Objects.requireNonNull(clientId);
    Objects.requireNonNull(service);
    Objects.requireNonNull(timeoutDuration);

    //Set first state to failed.
    this.state = ResponseState.FAILED;
    this.stateMap = StateMap.createMap();

    /*
     * Create client instance.
     */
    final HttpClient httpClient = HttpClient.create()
        //Cancel redirect
        .followRedirect(false)
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Math.toIntExact(timeoutDuration.toMillis()))
        .responseTimeout(timeoutDuration);

    this.webClient = WebClient.builder()
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

    // Set active
    this.active = true;

    log.info("Successfully created client to '{}'.", service);
    log.info("Starting client monitoring...");

    final Thread thread = Thread.currentThread();

    //Start new thread.
    new Thread(() -> {
      //Run as long client runs.
      while (!thread.isInterrupted() && this.active) {
        try {
          //To keep performance height
          Thread.sleep(500L);
        } catch (final InterruptedException ignore) {
          //Ignore.
        }

        //Ping if time is positive.
        if (this.lastStateChange-System.currentTimeMillis()<=0) {
          //Set new ping
          this.ping();
          this.totalPingCount++;
        }
      }
      //Kill thread if main is also shut down.
      log.info("Client monitoring stopped.");
    })
        .start();

    log.info("Trying to initialize connection.");
    //First synchronized ping to validate client.
    this.ping();
  }

  public WebRestClient(@Nullable final String clientId) {
    this(clientId, "http://localhost:8443", Duration.ofSeconds(5));

    log.info("Created default(test) client.");
  }

  public WebRestClient(@Nullable final String clientId,
                       @Nullable final String service) {
    this(clientId, service, Duration.ofSeconds(5));
  }

  /**
   * See {@link IWebRestClient#get(String, Class)}.
   */
  @Override
  public <RESPONSE> @NotNull RESPONSE get(@Nullable String apiEndpoint,
                                          @Nullable Class<RESPONSE> responseClass) {
    return this.response(apiEndpoint, HttpMethod.GET, responseClass, null);
  }

  /**
   * See {@link IWebRestClient#get(String, Class, Object)}.
   */
  @Override
  public <RESPONSE, TYPE> @NotNull RESPONSE get(@Nullable String apiEndpoint,
                                                @Nullable Class<RESPONSE> responseClass,
                                                @Nullable TYPE body) {
    return this.response(apiEndpoint, HttpMethod.GET, responseClass, body);
  }

  /**
   * See {@link IWebRestClient#put(String, Class)}.
   */
  @Override
  public <RESPONSE> @NotNull RESPONSE put(@Nullable String apiEndpoint,
                                          @Nullable Class<RESPONSE> responseClass) {
    return this.response(apiEndpoint, HttpMethod.PUT, responseClass, null);
  }

  /**
   * See {@link IWebRestClient#put(String, Class, Object)}.
   */
  public <RESPONSE, TYPE> @NotNull RESPONSE put(@Nullable String apiEndpoint,
                                                @Nullable Class<RESPONSE> responseClass,
                                                @Nullable TYPE type) {
    return this.response(apiEndpoint, HttpMethod.PUT, responseClass, type);
  }

  /**
   * See {@link IWebRestClient#put(String, Class)}s
   */
  @Override
  public <RESPONSE> @NotNull RESPONSE post(@Nullable String apiEndpoint,
                                           @Nullable Class<RESPONSE> responseClass) {
    return this.response(apiEndpoint, HttpMethod.POST, responseClass, null);
  }

  /**
   * See {@link IWebRestClient#post(String, Class, Object)}.
   */
  public <RESPONSE, TYPE> @NotNull RESPONSE post(@Nullable String apiEndpoint,
                                                 @Nullable Class<RESPONSE> responseClass,
                                                 @Nullable TYPE type) {
    return this.response(apiEndpoint, HttpMethod.POST, responseClass, type);
  }

  /**
   * See {@link IWebRestClient#delete(String, Class)}.
   */
  @Override
  public <RESPONSE> @NotNull RESPONSE delete(@Nullable String apiEndpoint,
                                             @Nullable Class<RESPONSE> responseClass) {
    return this.response(apiEndpoint, HttpMethod.DELETE, responseClass, null);
  }

  /**
   * See {@link IWebRestClient#delete(String, Class, Object)}.
   */
  public <RESPONSE, TYPE> @NotNull RESPONSE delete(@Nullable String apiEndpoint,
                                                   @Nullable Class<RESPONSE> responseClass,
                                                   @Nullable TYPE type) {
    return this.response(apiEndpoint, HttpMethod.DELETE, responseClass, type);
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

    //Time enabled.
    final long start = System.currentTimeMillis();
    final RequestBodySpec request = this.baseRequestSpec(apiEndpoint, httpMethod);

    //If type is present -> set content of request.
    if (type != null) {
      //Insert object will be converted to json.
      request.body(BodyInserters.fromValue(type));
    }

    return request.exchangeToMono(clientResponse -> {
          /*
           * Only  build object if ok.
           */
          if (this.validResponse(clientResponse)) {
            final long responseTime = System.currentTimeMillis()-start;
            //If client monitoring is active reset.
            this.lastState(ResponseState.SUCCESS);

            //Check for longest response
            if (this.longestResponseTime<responseTime) {
              //Set new time
              this.longestResponseTime = responseTime;
              log.debug("New longest response time defined. Time={}ms", this.longestResponseTime);
            }

            return clientResponse.bodyToMono(typeClass);
          }
          //If client monitoring is active error.
          this.lastState(ResponseState.FAILED);

          return clientResponse.createError();
        })

        //Block until response.
        .block();
  }

  private @NotNull WebClient.RequestBodySpec baseRequestSpec(@NotNull final String endPoint,
                                                             @NotNull final HttpMethod httpMethod) {
    final UriSpec<RequestBodySpec> requestBodySpec = this
        .webClient
        .method(httpMethod);


    return requestBodySpec
        //Url
        .uri(endPoint)
        .acceptCharset(StandardCharsets.UTF_8);
  }

  /**
   * True if {@link ClientResponse} was valid.
   */
  private boolean validResponse(@NotNull final ClientResponse clientResponse) {
    return clientResponse.statusCode().is2xxSuccessful()
        || clientResponse.statusCode().value() == 409;
  }

  @Override
  public @NotNull Long ping() {
    //Time enabled.
    final long start = System.currentTimeMillis();

    ResponseState state;
    try {
      state = this
          //Ping any node.
          .baseRequestSpec("/api/v1/ping", HttpMethod.POST)
          //Handle response
          .exchangeToMono(clientResponse -> {
            //Check if response was success.
            if (clientResponse.statusCode().is2xxSuccessful()) {
              //Ok
              return Mono.just(ResponseState.SUCCESS);
            }
            //Error code -> error in communication, wrong coding or server down.
            return Mono.just(ResponseState.FAILED);
          })
          //Block thread until server response.
          .block();
    } catch (final Exception exception) {
      //If connections fails (no server) an error will be thrown.
      state = ResponseState.FAILED;
    }

    //Calculate time request took.
    final long duration = (System.currentTimeMillis()-start);

    if (state == ResponseState.SUCCESS) {
      log.info("API endpoint available Took {}ms.", duration);
      this.lastState(ResponseState.SUCCESS);
    } else {
      log.warn("No response from API endpoint. Took {}ms.", duration);
      this.lastState(ResponseState.FAILED);
    }

    return duration;
  }

  @Override
  public @NotNull IWebRestClient shutdown() {
    //stop if active.
    if (this.active) {
      this.active = false;
      log.info("Stopping client.");
    }
    return this;
  }

  @Override
  public @NotNull StateHandler<ResponseState> handle(@Nullable ResponseState responseState,
                                                     @Nullable ThrowableRunnable runnable) {
    //Null check
    Objects.requireNonNull(responseState);
    Objects.requireNonNull(runnable);

    //Add state.
    this.stateMap.append(responseState, runnable);
    return this;
  }

  private void lastState(@NotNull final ResponseState lastState) {
    this.lastStateChange = System.currentTimeMillis()+DEFAULT_PING_INTERVAL;
    //Ignore change
    if (this.state == lastState && this.totalPingCount>0) {
      return;
    }
    this.state = lastState;

    if (this.state == ResponseState.SUCCESS) {
      log.info("Client connection established.");

      this.stateMap.executeRunnable(ResponseState.SUCCESS);
      return;
    }
    log.warn("Client connection failed.");
    this.stateMap.executeRunnable(ResponseState.FAILED);
  }
}
