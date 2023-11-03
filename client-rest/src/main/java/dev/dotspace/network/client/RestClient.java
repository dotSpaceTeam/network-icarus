package dev.dotspace.network.client;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.dotspace.common.function.ThrowableRunnable;
import dev.dotspace.network.library.exception.ClientNotActiveException;
import dev.dotspace.network.client.message.IMessageRequest;
import dev.dotspace.network.client.position.IPositionRequest;
import dev.dotspace.network.client.profile.IProfileRequest;
import dev.dotspace.network.client.web.ClientState;
import dev.dotspace.network.client.web.IWebClient;
import dev.dotspace.network.library.Library;
import dev.dotspace.network.library.system.participant.IParticipant;
import dev.dotspace.network.library.system.participant.ImmutableParticipant;
import dev.dotspace.network.library.system.participant.ParticipantType;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;


@Log4j2
@Accessors(fluent=true)
public final class RestClient implements IRestClient {
  @Getter
  private final @NotNull IParticipant participant;
  /**
   * Manager to hold provider for client.
   */
  private final @NotNull Injector injector;

  /**
   * Only local .
   */
  private RestClient(@NotNull final String endPoint) {
    //Define runtime.
    this.participant = ImmutableParticipant.randomOfType(ParticipantType.CLIENT);

    //Create injector.
    this.injector = Guice.createInjector(
        new RestClientModule(this.participant().identifier(), endPoint), Library.module());
  }

  /**
   * Pass to {@link IWebClient#handle(Object, ThrowableRunnable)} .
   */
  @Override
  public @NotNull IRestClient handle(@Nullable ClientState clientState,
                                     @Nullable ThrowableRunnable runnable) {
    this.injector.getInstance(IWebClient.class).handle(clientState, runnable);
    return this;
  }

  /**
   * See {@link IRestClient#profileRequest()}
   */
  @Override
  public @NotNull IProfileRequest profileRequest() {
    return this.request(IProfileRequest.class);
  }

  /**
   * See {@link IRestClient#profileRequest()}
   */
  @Override
  public @NotNull IPositionRequest positionRequest() {
    return this.request(IPositionRequest.class);
  }


  /**
   * See {@link IRestClient#messageRequest()}
   */
  @Override
  public @NotNull IMessageRequest messageRequest() {
    return this.request(IMessageRequest.class);
  }

  //Get provider else throw
  private <REQUEST> @NotNull REQUEST request(@NotNull final Class<REQUEST> requestClass) {
    return this.injector.getInstance(requestClass);
  }

  //static
  private static @Nullable RestClient client;
  //Check if client is enabled.

  /**
   * Get client instance.
   *
   * @return get singleton instance.
   */
  public static @NotNull IRestClient client() {
    return Optional
        //Get client
        .ofNullable(client)
        //Else error
        .orElseThrow(ClientNotActiveException::new);
  }

  /**
   * Enable if not enabled.
   */
  public static void connect(@Nullable final String endPoint) {
    //Null check
    Objects.requireNonNull(endPoint);

    //Already enabled.
    if (RestClient.present()) {
      log.warn("Client already enabled.");
      return;
    }

    //Init client
    client = new RestClient(endPoint);
    log.info("Enabled client.");
  }

  public static void disconnect() {
    //Ignore if client is not present.
    if (client == null) {
      return;
    }

    //Shutdown reset client.
    client.injector.getInstance(IWebClient.class).shutdown();

    //Set client to null.
    client = null;

    log.info("Disabled client.");
  }

  public static boolean present() {
    return client != null;
  }

  /**
   * Check if client is connected.
   */
  public static boolean connected() {
    return client != null && client.injector.getInstance(IWebClient.class).state() == ClientState.ESTABLISHED;
  }

  /**
   * Check if client is disconnected, only so if enabled and last connections failed.
   */
  public static boolean disconnected() {
    return client != null && client.injector.getInstance(IWebClient.class).state() == ClientState.FAILED;
  }
}