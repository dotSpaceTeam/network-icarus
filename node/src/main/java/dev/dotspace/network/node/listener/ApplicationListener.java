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

  @SuppressWarnings("all")
  @EventListener
  public void handleEvent(@NotNull final ApplicationReadyEvent event) {
    //Log begin.
    log.info("Registering node as participant...");

    System.out.println("""
                                                                                            \s
                                                                 dddddddd                   \s
            NNNNNNNN        NNNNNNNN                             d::::::d                   \s
            N:::::::N       N::::::N                             d::::::d                   \s
            N::::::::N      N::::::N                             d::::::d                   \s
            N:::::::::N     N::::::N                             d:::::d                    \s
            N::::::::::N    N::::::N   ooooooooooo       ddddddddd:::::d     eeeeeeeeeeee   \s
            N:::::::::::N   N::::::N oo:::::::::::oo   dd::::::::::::::d   ee::::::::::::ee \s
            N:::::::N::::N  N::::::No:::::::::::::::o d::::::::::::::::d  e::::::eeeee:::::ee
            N::::::N N::::N N::::::No:::::ooooo:::::od:::::::ddddd:::::d e::::::e     e:::::e
            N::::::N  N::::N:::::::No::::o     o::::od::::::d    d:::::d e:::::::eeeee::::::e
            N::::::N   N:::::::::::No::::o     o::::od:::::d     d:::::d e:::::::::::::::::e\s
            N::::::N    N::::::::::No::::o     o::::od:::::d     d:::::d e::::::eeeeeeeeeee \s
            N::::::N     N:::::::::No::::o     o::::od:::::d     d:::::d e:::::::e          \s
            N::::::N      N::::::::No:::::ooooo:::::od::::::ddddd::::::dde::::::::e         \s
            N::::::N       N:::::::No:::::::::::::::o d:::::::::::::::::d e::::::::eeeeeeee \s
            N::::::N        N::::::N oo:::::::::::oo   d:::::::::ddd::::d  ee:::::::::::::e \s
            NNNNNNNN         NNNNNNN   ooooooooooo      ddddddddd   ddddd    eeeeeeeeeeeeee \s
                                                                                            \s
                                                                                            \s
        """);

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
