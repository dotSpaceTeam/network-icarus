package dev.dotspace.network.node.web.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;


public record ImmutableErrorResponse(long timestamp,
                                     @NotNull HttpStatus status,
                                     @NotNull String message
) implements IResponse {

  @Override
  public int code() {
    return this.status.value();
  }

  public static @NotNull IResponse create(@NotNull final HttpStatus httpStatus,
                                          @NotNull final String message) {
    return new ImmutableErrorResponse(System.currentTimeMillis(), httpStatus, message);
  }

}
