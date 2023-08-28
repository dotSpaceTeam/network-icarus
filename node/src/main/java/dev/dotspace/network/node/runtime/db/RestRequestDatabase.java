package dev.dotspace.network.node.runtime.db;

import dev.dotspace.common.SpaceLibrary;
import dev.dotspace.common.response.Response;
import dev.dotspace.network.node.database.AbstractDatabase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;


@Component("restRequestDatabase")
public final class RestRequestDatabase extends AbstractDatabase {
  /**
   * Manipulate {@link IRestRequest}.
   */
  @Autowired
  private RestRequestRepository requestRepository;

  public @NotNull Response<IRestRequest> createRequestInfo(@Nullable final String url,
                                                           @Nullable final String client,
                                                           @Nullable final String method,
                                                           final long processTime,
                                                           final boolean success,
                                                           @Nullable final String note,
                                                           @Nullable final Date timestamp) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(url);
      Objects.requireNonNull(client);
      Objects.requireNonNull(method);
      Objects.requireNonNull(timestamp);

      return this.requestRepository.save(new RestRequestEntity(url, client, method, processTime, success, note, timestamp));
    });
  }

}
