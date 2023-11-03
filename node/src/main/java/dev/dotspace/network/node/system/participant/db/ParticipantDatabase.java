package dev.dotspace.network.node.system.participant.db;

import dev.dotspace.network.library.system.participant.IParticipant;
import dev.dotspace.network.library.system.participant.ImmutableParticipant;
import dev.dotspace.network.library.system.participant.ParticipantType;
import dev.dotspace.network.node.database.AbstractDatabase;
import dev.dotspace.network.node.database.exception.EntityAlreadyPresentException;
import dev.dotspace.network.node.database.exception.EntityNotPresentException;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@Log4j2
public final class ParticipantDatabase extends AbstractDatabase {
  /**
   * Manipulate data.
   */
  @Autowired
  private ParticipantRepository participantRepository;

  public @NotNull IParticipant createParticipant(@Nullable final String identifier,
                                                 @Nullable final ParticipantType type) throws EntityAlreadyPresentException {
    //Null check
    Objects.requireNonNull(identifier);
    Objects.requireNonNull(type);

    //Check if already present.
    if (this.participantRepository.findByIdentifier(identifier).isPresent()) {
      throw new EntityAlreadyPresentException(null, "Session id="+identifier+" already present.");
    }

    final ParticipantEntity participantEntity = new ParticipantEntity(identifier, type);
    //Store.
    this.participantRepository.save(participantEntity);
    return ImmutableParticipant.of(participantEntity);
  }

  public boolean existsParticipant(@Nullable String identifier) {
    //Null check
    Objects.requireNonNull(identifier);

    //Get participant
    return this.participantRepository.existsByIdentifier(identifier);
  }

  public @NotNull IParticipant getParticipant(@Nullable String identifier) throws EntityNotPresentException {
    //Null check
    Objects.requireNonNull(identifier);

    return this.participantRepository
        //Get participant
        .findByIdentifier(identifier)
        //Map to plain
        .map(ImmutableParticipant::of)
        //Else not present.
        .orElseThrow(() -> new EntityNotPresentException("No participant found with id="+identifier+"."));
  }
}
