package dev.dotspace.network.node.listener;

import dev.dotspace.network.library.connection.IRestRequest;
import dev.dotspace.network.library.system.participant.ParticipantType;
import dev.dotspace.network.node.connection.db.RestRequestDatabase;
import dev.dotspace.network.node.database.exception.EntityAlreadyPresentException;
import dev.dotspace.network.node.system.participant.db.ParticipantDatabase;
import dev.dotspace.network.node.web.event.ClientAddEvent;
import dev.dotspace.network.node.web.event.ClientRemoveEvent;
import dev.dotspace.network.node.web.event.RequestResponseEvent;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.regex.Pattern;


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
      } catch (final EntityAlreadyPresentException exception) {
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

    //If true, request will be logged.
    final boolean logRequest = this.logRequest(restRequest.path());

    //Log
    log.info("[{}]'{}' took {} ms. (status={}, log={})",
        restRequest.method(), restRequest.path(), restRequest.processTime(), restRequest.status(), logRequest);

    //No further operation with pseudo.
    if (!logRequest) {
      return;
    }

    //Add to database.
    this.executor.execute(() -> {
      //Log request.
      this.restRequestDatabase.createRequestInfo(
          restRequest.path(),
          //Get client identifier
          restRequest.client().identifier(),
          restRequest.method(),
          restRequest.address(),
          restRequest.note(),
          restRequest.timestamp(),
          restRequest.processTime(),
          restRequest.status());
    });
  }

  /**
   * Pattern for request
   */
  private final static Pattern LOG_PATTERN = Pattern.compile("/api/v[0-9]/(?!ping).*");

  /**
   * If url is a pseudo url -> will not be logged.
   */
  private boolean logRequest(@NotNull final String url) {
    //Check if url is loggable.
    return LOG_PATTERN.matcher(url).find();
  }
}
