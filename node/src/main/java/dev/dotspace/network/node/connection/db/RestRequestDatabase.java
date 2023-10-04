package dev.dotspace.network.node.connection.db;

import dev.dotspace.network.library.connection.IRestRequest;
import dev.dotspace.network.node.database.AbstractDatabase;
import dev.dotspace.network.node.system.db.ParticipantEntity;
import dev.dotspace.network.node.system.db.ParticipantRepository;
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
   * Manipulate {@link dev.dotspace.network.library.system.IParticipant}
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

    return this.restRequestRepository.save(
        new RestRequestEntity(path, participantEntity, method, address, note, timestamp, processTime, status));
  }
}