package dev.dotspace.network.rabbitmq;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


//Block construct.
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public final class RabbitField {
  public static @NotNull String APPLICATION_JSON = "application/json";
  public static @NotNull String MAPPER_CLASS = "schema";

}
