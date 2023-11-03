package dev.dotspace.network.client;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import dev.dotspace.network.client.message.IMessageRequest;
import dev.dotspace.network.client.message.MessageRequest;
import dev.dotspace.network.client.position.IPositionRequest;
import dev.dotspace.network.client.position.PositionRequest;
import dev.dotspace.network.client.profile.IProfileRequest;
import dev.dotspace.network.client.profile.ProfileRequest;
import dev.dotspace.network.client.web.IWebClient;
import dev.dotspace.network.client.web.WebClient;
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
    this.bind(IWebClient.class)
        .toInstance(new WebClient(this.identifier, this.endPoint));

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
