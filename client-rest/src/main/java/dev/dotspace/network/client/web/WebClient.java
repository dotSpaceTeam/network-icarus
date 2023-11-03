package dev.dotspace.network.client.web;

import dev.dotspace.common.function.ThrowableRunnable;
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
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;

@Accessors(fluent=true)
@Log4j2
public final class WebClient implements IWebClient {
  //################## Config values ######################
  /**
   * Amount of stored pings for average ping.
   */
  private final static int PING_COLLECTION = 128;
  private final static long DEFAULT_PING_INTERVAL = 10000L;

  /**
   * Spring webclient for request.
   */
  private final @NotNull org.springframework.web.reactive.function.client.WebClient webClient;
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
  private @NotNull ClientState state;
  /**
   * Store values of state runnable.
   */
  private final @NotNull StateMap<ClientState> stateMap;

  @SuppressWarnings("busy-waiting")
  public WebClient(@Nullable final String clientId,
                   @Nullable final String service,
                   @Nullable final Duration timeoutDuration) {
    //Null check
    Objects.requireNonNull(clientId);
    Objects.requireNonNull(service);
    Objects.requireNonNull(timeoutDuration);

    //Set first state to failed.
    this.state = ClientState.FAILED;
    this.stateMap = StateMap.createMap();

    /*
     * Create client instance.
     */
    final HttpClient httpClient = HttpClient.create()
        //Cancel redirect
        .followRedirect(false)
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
          //Ignore.
        } catch (final InterruptedException ignore) {
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

  public WebClient(@Nullable final String clientId) {
    this(clientId, "http://localhost:8443", Duration.ofSeconds(5));

    log.info("Created default(test) client.");
  }

  public WebClient(@Nullable final String clientId,
                   @Nullable final String service) {
    this(clientId, service, Duration.ofSeconds(5));
  }

  /**
   * See {@link IWebClient#get(String, Class)}.
   */
  @Override
  public <RESPONSE> @NotNull RESPONSE get(@Nullable String apiEndpoint,
                                          @Nullable Class<RESPONSE> responseClass) {
    return this.response(apiEndpoint, HttpMethod.GET, responseClass, null);
  }

  /**
   * See {@link IWebClient#get(String, Class, Object)}.
   */
  @Override
  public <RESPONSE, TYPE> @NotNull RESPONSE get(@Nullable String apiEndpoint,
                                                @Nullable Class<RESPONSE> responseClass,
                                                @Nullable TYPE body) {
    return this.response(apiEndpoint, HttpMethod.GET, responseClass, body);
  }

  /**
   * See {@link IWebClient#put(String, Class)}.
   */
  @Override
  public <RESPONSE> @NotNull RESPONSE put(@Nullable String apiEndpoint,
                                          @Nullable Class<RESPONSE> responseClass) {
    return this.response(apiEndpoint, HttpMethod.PUT, responseClass, null);
  }

  /**
   * See {@link IWebClient#put(String, Class, Object)}.
   */
  public <RESPONSE, TYPE> @NotNull RESPONSE put(@Nullable String apiEndpoint,
                                                @Nullable Class<RESPONSE> responseClass,
                                                @Nullable TYPE type) {
    return this.response(apiEndpoint, HttpMethod.PUT, responseClass, type);
  }

  /**
   * See {@link IWebClient#put(String, Class)}s
   */
  @Override
  public <RESPONSE> @NotNull RESPONSE post(@Nullable String apiEndpoint,
                                           @Nullable Class<RESPONSE> responseClass) {
    return this.response(apiEndpoint, HttpMethod.POST, responseClass, null);
  }

  /**
   * See {@link IWebClient#post(String, Class, Object)}.
   */
  public <RESPONSE, TYPE> @NotNull RESPONSE post(@Nullable String apiEndpoint,
                                                 @Nullable Class<RESPONSE> responseClass,
                                                 @Nullable TYPE type) {
    return this.response(apiEndpoint, HttpMethod.POST, responseClass, type);
  }

  /**
   * See {@link IWebClient#delete(String, Class)}.
   */
  @Override
  public <RESPONSE> @NotNull RESPONSE delete(@Nullable String apiEndpoint,
                                             @Nullable Class<RESPONSE> responseClass) {
    return this.response(apiEndpoint, HttpMethod.DELETE, responseClass, null);
  }

  /**
   * See {@link IWebClient#delete(String, Class, Object)}.
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
            this.lastState(ClientState.ESTABLISHED);

            //Check for longest response
            if (this.longestResponseTime<responseTime) {
              //Set new time
              this.longestResponseTime = responseTime;
              log.debug("New longest response time defined. Time={}ms", this.longestResponseTime);
            }

            return clientResponse.bodyToMono(typeClass);
          }
          //If client monitoring is active error.
          this.lastState(ClientState.FAILED);

          return clientResponse.createError();
        })

        //Block until response.
        .block();
  }

  private @NotNull org.springframework.web.reactive.function.client.WebClient.RequestBodySpec baseRequestSpec(@NotNull final String endPoint,
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

    ClientState state;
    try {
      state = this
          //Ping any node.
          .baseRequestSpec("/api/v1/ping", HttpMethod.GET)
          //Handle response
          .exchangeToMono(clientResponse -> {
            //Check if response was success.
            if (clientResponse.statusCode().is2xxSuccessful()) {
              //Ok
              return Mono.just(ClientState.ESTABLISHED);
            }
            //Error code -> error in communication, wrong coding or server down.
            return Mono.just(ClientState.FAILED);
          })
          //Block thread until server response.
          .block();
    } catch (final Exception exception) {
      //If connections fails (no server) an error will be thrown.
      state = ClientState.FAILED;
    }

    //Calculate time request took.
    final long duration = (System.currentTimeMillis()-start);

    if (state == ClientState.ESTABLISHED) {
      log.info("API endpoint available Took {}ms.", duration);
      this.lastState(ClientState.ESTABLISHED);
    } else {
      log.warn("No response from API endpoint. Took {}ms.", duration);
      this.lastState(ClientState.FAILED);
    }

    return duration;
  }

  @Override
  public @NotNull IWebClient shutdown() {
    //stop if active.
    if (this.active) {
      this.active = false;
      log.info("Stopping client.");
    }
    return this;
  }

  @Override
  public @NotNull StateHandler<ClientState> handle(@Nullable ClientState clientState,
                                                   @Nullable ThrowableRunnable runnable) {
    //Null check
    Objects.requireNonNull(clientState);
    Objects.requireNonNull(runnable);

    //Add state.
    this.stateMap.append(clientState, runnable);
    return this;
  }

  private void lastState(@NotNull final ClientState lastState) {
    this.lastStateChange = System.currentTimeMillis()+DEFAULT_PING_INTERVAL;
    //Ignore change
    if (this.state == lastState && this.totalPingCount>0) {
      return;
    }
    this.state = lastState;

    if (this.state == ClientState.ESTABLISHED) {
      log.info("Client connection established.");

      this.stateMap.executeRunnable(ClientState.ESTABLISHED);
      return;
    }
    log.warn("Client connection failed.");
    this.stateMap.executeRunnable(ClientState.FAILED);
  }
}
