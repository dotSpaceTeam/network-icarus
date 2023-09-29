package dev.dotspace.network.library.connection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Objects;


public record ImmutableRestRequest(@NotNull String path,
                                   @NotNull String client,
                                   @NotNull String method,
                                   @NotNull String address,
                                   @NotNull String note,
                                   @NotNull Date timestamp,
                                   long processTime,
                                   int status
) implements IRestRequest {
  /**
   * Convert {@link IRestRequest} to {@link ImmutableRestRequest}.
   *
   * @param restRequest to convert.
   * @return instance of {@link IRestRequest}.
   */
  public static @NotNull IRestRequest of(@Nullable final IRestRequest restRequest) {
    //Null check
    Objects.requireNonNull(restRequest);

    return new ImmutableRestRequest(restRequest.path(),
        restRequest.client(),
        restRequest.method(),
        restRequest.address(),
        restRequest.note(),
        restRequest.timestamp(),
        restRequest.processTime(),
        restRequest.status());
  }
}
