package dev.dotspace.network.node.listener;

import dev.dotspace.network.library.connection.IRestRequest;
import dev.dotspace.network.library.system.ParticipantType;
import dev.dotspace.network.node.connection.db.RestRequestDatabase;
import dev.dotspace.network.node.exception.ElementAlreadyPresentException;
import dev.dotspace.network.node.system.db.ParticipantDatabase;
import dev.dotspace.network.node.web.event.ClientAddEvent;
import dev.dotspace.network.node.web.event.ClientRemoveEvent;
import dev.dotspace.network.node.web.event.RequestResponseEvent;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;


@Component
@Log4j2
public final class WebListener {
  /**
   * Database for clients
   */
  @Autowired
  private ParticipantDatabase participantDatabase;
  /**
   * Reset database
   */
  @Autowired
  private RestRequestDatabase restRequestDatabase;

  /**
   * Async handle.
   */
  @Autowired
  private Executor executor;

  @EventListener
  public void handleClientAdd(@NotNull final ClientAddEvent event) {
    //Id
    final String clientId = event.clientId();

    //Absent -> create.
    if (!this.participantDatabase.existsParticipant(clientId)) {
      //Add to db.
      try {
        //Create new entry
        this.participantDatabase.createParticipant(clientId, ParticipantType.CLIENT);

        //log creation
        log.info("Registered client={} as new connection.", clientId);
      } catch (final ElementAlreadyPresentException exception) {
        log.warn("Error while registering client="+clientId+".", exception);
      }
    } else {
      //log if back online.
      log.info("Connection to client={} reestablished.", clientId);
    }
  }

  @EventListener
  public void handleClientAdd(@NotNull final ClientRemoveEvent event) {
    //Id
    final String clientId = event.clientId();

    log.info("Connection to client={} expired.", clientId);
  }

  @EventListener
  public void handleRequest(@NotNull final RequestResponseEvent event) {
    //Id
    final IRestRequest restRequest = event.request();

    //If true, request will not be logged.
    final boolean pseudoRequest = this.pseudoRequest(restRequest.path());

    //Log
    log.info("[{}]'{}' took {} ms. (status={}, pseudo={})",
        restRequest.method(), restRequest.path(), restRequest.processTime(), restRequest.status(), pseudoRequest);

    //No further operation with pseudo.
    if (pseudoRequest) {
      return;
    }

    //Add to database.
    this.executor.execute(() -> {
      //Log request.
      this.restRequestDatabase.createRequestInfo(
          restRequest.path(),
          restRequest.client(),
          restRequest.method(),
          restRequest.address(),
          restRequest.note(),
          restRequest.timestamp(),
          restRequest.processTime(),
          restRequest.status());
    });
  }

  /**
   * If url is a pseudo url -> will not be logged.
   */
  private boolean pseudoRequest(@NotNull final String url) {
    return url.endsWith("ping") ||
        //Fallback if something fails.
        url.endsWith("/error") ||
        //Block files
        url.contains(".html") ||
        url.contains(".ico");
  }
}
