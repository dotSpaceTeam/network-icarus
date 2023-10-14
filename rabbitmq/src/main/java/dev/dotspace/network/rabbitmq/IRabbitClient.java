package dev.dotspace.network.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import dev.dotspace.network.rabbitmq.exception.RabbitClientAbsentException;
import dev.dotspace.network.rabbitmq.exception.RabbitIOException;
import dev.dotspace.network.rabbitmq.publisher.IRabbitPublisher;
import dev.dotspace.network.rabbitmq.subscriber.IRabbitSubscriber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;


/**
 * Client for rabbit mq operation.
 */
public interface IRabbitClient {
  @NotNull ConnectionFactory connectionFactory();

  @NotNull Connection connection() throws RabbitClientAbsentException;

  @NotNull Channel channel() throws RabbitClientAbsentException;

  @NotNull Channel newChannel() throws RabbitClientAbsentException;

  @NotNull IRabbitPublisher newPublisher();

  @NotNull IRabbitSubscriber newSubscriber(@Nullable final String exchange,
                                           @Nullable final String routingKey) throws RabbitClientAbsentException;

  @NotNull IRabbitClient createQueueIfAbsent(@Nullable final String name,
                                             final boolean durable,
                                             final boolean exclusive,
                                             final boolean autoDelete,
                                             @Nullable final Map<String, Object> arguments) throws RabbitIOException,
      RabbitClientAbsentException;

  @NotNull IRabbitClient createExchangeIfAbsent(@Nullable final String name,
                                                @Nullable final String type) throws RabbitIOException,
      RabbitClientAbsentException;
}
