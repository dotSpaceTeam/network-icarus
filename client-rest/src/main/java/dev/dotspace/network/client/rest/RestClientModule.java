package dev.dotspace.network.client.rest;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import dev.dotspace.network.client.rest.message.IMessageRequest;
import dev.dotspace.network.client.rest.message.MessageRequest;
import dev.dotspace.network.client.rest.position.IPositionRequest;
import dev.dotspace.network.client.rest.position.PositionRequest;
import dev.dotspace.network.client.rest.profile.IProfileRequest;
import dev.dotspace.network.client.rest.profile.ProfileRequest;
import dev.dotspace.network.client.rest.web.IWebRestClient;
import dev.dotspace.network.client.rest.web.WebRestClient;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;


/**
 * Configure google guice.
 */
@Log4j2
final class RestClientModule extends AbstractModule {

  private final @NotNull String identifier;
  private final @NotNull String endPoint;

  RestClientModule(@NotNull final String identifier,
                   @NotNull final String endPoint) {
    this.identifier = identifier;
    this.endPoint = endPoint;
  }

  @Override
  protected void configure() {
    log.info("Configuring ClientModule...");
    //Configure start.

    //Create rest client.
    this.bind(IWebRestClient.class)
        .toInstance(new WebRestClient(this.identifier, this.endPoint));

    //Requests.
    this.bind(IPositionRequest.class)
        .to(PositionRequest.class)
        .in(Scopes.SINGLETON);

    this.bind(IProfileRequest.class)
        .to(ProfileRequest.class)
        .in(Scopes.SINGLETON);

    this.bind(IMessageRequest.class)
        .to(MessageRequest.class)
        .in(Singleton.class);

    //Configure end.
    log.info("Client(ClientModule) configured.");
  }
}
