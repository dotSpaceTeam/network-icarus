package dev.dotspace.network.client.rest.web.param;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public record RequestParameter(@NotNull String key,
                               @NotNull String parameter) implements IRequestParameter {

  //static
  public static @NotNull IRequestParameter create(@Nullable final String key,
                                                  @Nullable final String parameter) {
    //Null check
    Objects.requireNonNull(key);
    Objects.requireNonNull(parameter);

    return new RequestParameter(key, parameter);
  }

}
