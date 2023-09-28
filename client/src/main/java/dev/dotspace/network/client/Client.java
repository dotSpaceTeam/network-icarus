package dev.dotspace.network.client;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.dotspace.common.function.ThrowableRunnable;
import dev.dotspace.network.client.message.IMessageRequest;
import dev.dotspace.network.client.position.IPositionRequest;
import dev.dotspace.network.client.profile.IProfileRequest;
import dev.dotspace.network.client.web.ClientState;
import dev.dotspace.network.client.web.IRestClient;
import dev.dotspace.network.library.Library;
import dev.dotspace.network.library.runtime.IRuntime;
import dev.dotspace.network.library.runtime.ImmutableRuntime;
import dev.dotspace.network.library.runtime.RuntimeType;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;


@Log4j2
@Accessors(fluent=true)
public final class Client implements IClient {
  /**
   * Runtime info.
   */
  private final @NotNull IRuntime runtime;
  /**
   * Manager to hold provider for client.
   */
  private final @NotNull Injector injector;

  /**
   * Thread client is running on.
   */
  @Getter
  private final @NotNull Thread thread;

  /**
   * Only local .
   */
  private Client(@NotNull final String endPoint) {
    this.runtime = ImmutableRuntime.randomOfType(RuntimeType.CLIENT);
    //Create injector.
    this.injector = Guice.createInjector(
        new ClientModule(this.runtime.runtimeId(), endPoint), Library.module());

    this.thread = Thread.currentThread();
    log.info("Client instance running under id={}.", this.runtime.runtimeId());
  }

  /**
   * Pass to {@link IRestClient#handle(Object, ThrowableRunnable)} .
   */
  @Override
  public @NotNull IClient handle(@Nullable ClientState clientState,
                                 @Nullable ThrowableRunnable runnable) {
    this.injector.getInstance(IRestClient.class).handle(clientState, runnable);
    return this;
  }

  /**
   * See {@link IClient#profileRequest()}
   */
  @Override
  public @NotNull IProfileRequest profileRequest() {
    return this.request(IProfileRequest.class);
  }

  /**
   * See {@link IClient#profileRequest()}
   */
  @Override
  public @NotNull IPositionRequest positionRequest() {
    return this.request(IPositionRequest.class);
  }


  /**
   * See {@link IClient#messageRequest()}
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
  private static @Nullable Client client;
  //Check if client is enabled.

  /**
   * Get client instance.
   *
   * @return get singleton instance.
   */
  public static @NotNull IClient client() {
    return Optional
        .ofNullable(client)
        .orElseThrow(() -> new RuntimeException());
  }

  /**
   * Enable if not enabled.
   */
  public static void connect(@Nullable final String endPoint) {
    //Null check
    Objects.requireNonNull(endPoint);

    //Already enabled.
    if (Client.enabled()) {
      log.warn("Client already enabled.");
      return;
    }

    //Init client
    client = new Client(endPoint);
    log.info("Enabled client.");
    log.info("Checking client status...");
  }

  public static boolean enabled() {
    return client != null;
  }

  /**
   * Check if client is disconnected, only so if enabled and last connections failed.
   */
  public static boolean disconnected() {
    return client != null && client.injector.getInstance(IRestClient.class).state() == ClientState.FAILED;
  }
}
