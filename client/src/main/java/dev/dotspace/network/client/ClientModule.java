package dev.dotspace.network.client;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import dev.dotspace.network.client.message.IMessageRequest;
import dev.dotspace.network.client.message.MessageRequest;
import dev.dotspace.network.client.monitoring.ClientMonitoring;
import dev.dotspace.network.client.position.IPositionRequest;
import dev.dotspace.network.client.position.PositionRequest;
import dev.dotspace.network.client.profile.IProfileRequest;
import dev.dotspace.network.client.profile.ProfileRequest;
import dev.dotspace.network.client.session.ISessionRequest;
import dev.dotspace.network.client.session.SessionRequest;
import dev.dotspace.network.client.status.IStatusRequest;
import dev.dotspace.network.client.status.StatusRequest;
import dev.dotspace.network.client.web.IRestClient;
import dev.dotspace.network.client.web.RestClient;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;


/**
 * Configure google guice.
 */
@Log4j2
final class ClientModule extends AbstractModule {

  private final @NotNull String identifier;

  ClientModule(@NotNull final String identifier) {
    this.identifier = identifier;
  }

  @Override
  protected void configure() {
    log.info("Configuring ClientModule...");
    //Configure start.

    //Define new monitoring
    final ClientMonitoring clientMonitoring = new ClientMonitoring();

    //Create rest client.
    this.bind(IRestClient.class).toInstance(new RestClient(this.identifier).clientMonitoring(clientMonitoring));

    //Bind clientMonitoring
    this.bind(ClientMonitoring.class).toInstance(clientMonitoring);

    //Requests.
    this.bind(IPositionRequest.class)
        .to(PositionRequest.class)
        .in(Scopes.SINGLETON);

    this.bind(IProfileRequest.class)
        .to(ProfileRequest.class)
        .in(Scopes.SINGLETON);

    this.bind(ISessionRequest.class)
        .to(SessionRequest.class)
        .in(Scopes.SINGLETON);

    this.bind(IStatusRequest.class)
        .to(StatusRequest.class)
        .in(Scopes.SINGLETON);

    this.bind(IMessageRequest.class)
        .to(MessageRequest.class)
        .in(Singleton.class);

    //Configure end.
    log.info("Client(ClientModule) configured.");
  }
}
