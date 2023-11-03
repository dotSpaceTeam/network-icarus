package dev.dotspace.network.node.connection.db;

import dev.dotspace.network.library.connection.IRestRequest;
import dev.dotspace.network.library.connection.ImmutableRestRequest;
import dev.dotspace.network.library.data.DataManipulation;
import dev.dotspace.network.library.system.participant.IParticipant;
import dev.dotspace.network.node.database.AbstractDatabase;
import dev.dotspace.network.node.system.participant.db.ParticipantEntity;
import dev.dotspace.network.node.system.participant.db.ParticipantRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;


@Component
public final class RestRequestDatabase extends AbstractDatabase {
  /**
   * Manipulate {@link IRestRequest}.
   */
  @Autowired
  private RestRequestRepository restRequestRepository;
  /**
   * Manipulate {@link IParticipant}
   */
  @Autowired
  private ParticipantRepository participantRepository;

  public @NotNull IRestRequest createRequestInfo(@Nullable final String path,
                                                 @Nullable final String client,
                                                 @Nullable final String method,
                                                 @Nullable final String address,
                                                 @Nullable final String note,
                                                 @Nullable final Date timestamp,
                                                 final long processTime,
                                                 final int status) {
    //Null check
    Objects.requireNonNull(path);
    Objects.requireNonNull(method);
    Objects.requireNonNull(address);
    Objects.requireNonNull(note);
    Objects.requireNonNull(timestamp);

    //Get participant.
    final ParticipantEntity participantEntity = Optional
        .ofNullable(client)
        //Get client.
        .flatMap(s -> this.participantRepository.findByIdentifier(s))
        //Or else null.
        .orElse(null);

    //Create request.
    final IRestRequest restRequest = ImmutableRestRequest.of(this.restRequestRepository.save(
        new RestRequestEntity(path, participantEntity, method, address, note, timestamp, processTime, status)));

    //Fire event -> Create request.
    this.publishEvent(restRequest, ImmutableRestRequest.class, DataManipulation.CREATE);

    //Return
    return restRequest;
  }
}