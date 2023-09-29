package dev.dotspace.network.node.connection.db;

import dev.dotspace.network.library.connection.IRestRequest;
import dev.dotspace.network.library.connection.ImmutableRestRequest;
import dev.dotspace.network.node.database.AbstractDatabase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;


@Component
public final class RestRequestDatabase extends AbstractDatabase {
  /**
   * Manipulate {@link IRestRequest}.
   */
  @Autowired
  private RestRequestRepository requestRepository;

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
    Objects.requireNonNull(client);
    Objects.requireNonNull(method);
    Objects.requireNonNull(address);
    Objects.requireNonNull(note);
    Objects.requireNonNull(timestamp);

    return this.requestRepository.save(
        new RestRequestEntity(path, client, method, address, note, timestamp, processTime, status));
  }
}