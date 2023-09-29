package dev.dotspace.network.node.listener;

import dev.dotspace.network.library.system.IParticipant;
import dev.dotspace.network.node.exception.ElementAlreadyPresentException;
import dev.dotspace.network.node.system.db.ParticipantDatabase;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@Log4j2
public final class ApplicationListener {
  /**
   * Database to store participant
   */
  @Autowired
  private ParticipantDatabase participantDatabase;
  /**
   * Get local participant
   */
  @Autowired
  private IParticipant participant;

  @EventListener
  public void handleEvent(@NotNull final ApplicationReadyEvent event) {
    //Log begin.
    log.info("Registering node as participant...");

    try {
      this.participantDatabase.createParticipant(this.participant.identifier(), this.participant.type());
      //log success.
      log.info("Registered node={}[{}].", this.participant.identifier(), this.participant.type());
    } catch (final ElementAlreadyPresentException exception) {
      //log error.
      log.warn("Error while registering node.", exception);
    }
  }
}
