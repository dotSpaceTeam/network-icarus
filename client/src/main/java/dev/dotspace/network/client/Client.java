package dev.dotspace.network.client;

import dev.dotspace.network.client.position.PositionRequest;
import dev.dotspace.network.client.profile.ProfileRequest;
import dev.dotspace.network.client.session.SessionRequest;
import dev.dotspace.network.client.status.IStatusRequest;
import dev.dotspace.network.client.status.StatusRequest;
import dev.dotspace.network.client.web.IRestClient;
import dev.dotspace.network.client.web.RestClient;
import dev.dotspace.network.library.IkarusRuntime;
import dev.dotspace.network.library.position.IPositionManipulator;
import dev.dotspace.network.library.profile.IProfileManipulator;
import dev.dotspace.network.library.provider.IProviderManager;
import dev.dotspace.network.library.provider.Provider;
import dev.dotspace.network.library.provider.ProviderManager;
import dev.dotspace.network.library.session.ISessionManipulator;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Log4j2
public final class Client implements IClient {
  /**
   * Manager to hold provider for client.
   */
  private final @NotNull IProviderManager providerManager;

  /**
   * Only local .
   */
  private Client() {
    this.providerManager = new ProviderManager();
  }

  private void init() {
    log.info("Setting up client.");
    final IRestClient restClient = new RestClient();

    //Set up provider manager
    this.providerManager
      //Add providers.
      .provider(new StatusRequest(restClient))
      .provider(new PositionRequest(restClient))
      .provider(new ProfileRequest(restClient))
      .provider(new SessionRequest(restClient));

    log.info("Client ready.");
  }

  @Override
  public @NotNull IStatusRequest statusRequest() {
    return this.requestProvider(StatusRequest.class);
  }

  @Override
  public @NotNull IProfileManipulator profileRequest() {
    return this.requestProvider(ProfileRequest.class);
  }

  @Override
  public @NotNull IPositionManipulator positionRequest() {
    return this.requestProvider(PositionRequest.class);
  }

  @Override
  public @NotNull ISessionManipulator sessionRequest() {
    return this.requestProvider(SessionRequest.class);
  }

  //Get provider else throw
  private <PROVIDER extends Provider> @NotNull PROVIDER requestProvider(@NotNull final Class<PROVIDER> providerClass) {
    return this.providerManager.providerElseThrow(providerClass);
  }

  //static
  private final static @NotNull Client client = new Client();
  //Check if client is enabled.
  private static boolean enabled = false;

  public static @NotNull IClient client() {
    return client;
  }

  public static void enable() {
    //Already enabled.
    if (enabled) {
      log.warn("Client already enabled.");
      return;
    }
    //Init client
    client.init();
    enabled = true;
    log.info("Enabled client.");
    log.info("Checking client status...");

    //Time enabled.
    final long start = System.currentTimeMillis();
    //Send request to api
    client
      .statusRequest()
      .getState()
      //All fine.
      .ifPresent(state ->
        log.info("API endpoint available(Status={}). Took {}ms.", state.state(), (System.currentTimeMillis() - start)))
      //If request is null.
      .ifAbsent(() -> log.warn("Empty response from endpoint. Took {}ms.", (System.currentTimeMillis() - start)))
      //If api request fails.
      .ifExceptionally(throwable ->
        log.error("Error while contacting endpoint({}). Took {}ms.",
          Optional
            .ofNullable(throwable)
            .map(Throwable::getMessage)
            .orElse(null), (System.currentTimeMillis() - start)));
    //End of request
  }
}
