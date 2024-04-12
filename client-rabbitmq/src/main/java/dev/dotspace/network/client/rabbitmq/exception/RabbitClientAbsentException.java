package dev.dotspace.network.client.rabbitmq.exception;

import com.rabbitmq.client.ConnectionFactory;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public final class RabbitClientAbsentException extends RabbitException {
  /**
   * Create exception.
   *
   * @param host of client -> host address.
   * @param port of client.
   */
  public RabbitClientAbsentException(@Nullable final String host,
                                     final int port) {
    super("RabbitMq connection to host="+host+":"+port+" is not valid.");
  }

  public RabbitClientAbsentException(@Nullable final ConnectionFactory connectionFactory) {
    //Parse content.
    this(Objects.requireNonNull(connectionFactory).getHost(), connectionFactory.getPort());
  }
}
