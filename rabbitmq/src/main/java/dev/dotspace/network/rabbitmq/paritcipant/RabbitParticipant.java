package dev.dotspace.network.rabbitmq.paritcipant;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dotspace.network.rabbitmq.IRabbitClient;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


@Getter
@Accessors(fluent=true)
public abstract class RabbitParticipant {
  /**
   * Channel for rabbitmq.
   */
  private final @NotNull IRabbitClient rabbitClient;

  private final @NotNull ObjectMapper mapper;

  /**
   * Create participant.
   *
   * @param rabbitClient to use participant.
   */
  protected RabbitParticipant(@Nullable final IRabbitClient rabbitClient) {
    //Null check
    Objects.requireNonNull(rabbitClient);

    this.rabbitClient = rabbitClient;
    this.mapper = new ObjectMapper();
  }
}
