package dev.dotspace.network.node.web.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;


public record ImmutableBodyResponse<TYPE>(@NotNull TYPE body,
                                          long timestamp,
                                          @NotNull HttpStatus status,
                                          @NotNull String message
) implements IBodyResponse<TYPE> {

  @Override
  public int code() {
    return this.status.value();
  }

  public static <TYPE> @NotNull IBodyResponse<TYPE> create(@NotNull final TYPE type,
                                                           @NotNull final HttpStatus httpStatus,
                                                           @NotNull final String message) {
    return new ImmutableBodyResponse<>(type, System.currentTimeMillis(), httpStatus, message);
  }

  public static <TYPE> @NotNull IBodyResponse<TYPE> create(@NotNull final TYPE type,
                                                 @NotNull final HttpStatus httpStatus) {
    return ImmutableBodyResponse.create(type, httpStatus, httpStatus.name());
  }

  public static <TYPE> @NotNull IBodyResponse<TYPE> create(@NotNull final TYPE type) {
    return ImmutableBodyResponse.create(type, HttpStatus.OK);
  }
}
