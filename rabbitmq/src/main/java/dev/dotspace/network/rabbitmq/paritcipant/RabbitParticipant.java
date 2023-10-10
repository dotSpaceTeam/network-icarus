package dev.dotspace.network.rabbitmq.paritcipant;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dotspace.network.rabbitmq.IRabbitConnection;
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
  private final @NotNull IRabbitConnection rabbitClient;
  /**
   * Mapper used to json to java object.
   */
  private final @NotNull ObjectMapper mapper;

  /**
   * Create participant.
   *
   * @param rabbitClient to use participant.
   */
  protected RabbitParticipant(@Nullable final IRabbitConnection rabbitClient) {
    //Null check
    Objects.requireNonNull(rabbitClient);

    this.rabbitClient = rabbitClient;
    //Define plain mapper.
    this.mapper = new ObjectMapper();
  }
}
