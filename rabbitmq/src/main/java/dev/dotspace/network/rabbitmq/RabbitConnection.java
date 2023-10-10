package dev.dotspace.network.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import dev.dotspace.network.rabbitmq.exception.RabbitClientAbsentException;
import dev.dotspace.network.rabbitmq.exception.RabbitIOException;
import dev.dotspace.network.rabbitmq.publisher.IRabbitPublisher;
import dev.dotspace.network.rabbitmq.publisher.RabbitPublisher;
import dev.dotspace.network.rabbitmq.subscriber.IRabbitSubscriber;
import dev.dotspace.network.rabbitmq.subscriber.RabbitSubscriber;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;


@Log4j2
@Accessors(fluent=true)
public final class RabbitConnection implements IRabbitConnection {
  @Getter
  private final @NotNull ConnectionFactory connectionFactory;
  private @Nullable Connection connection;
  private @Nullable Channel channel;

  public RabbitConnection(@Nullable final String user,
                          @Nullable final String password,
                          @Nullable final String virtualHost,
                          @Nullable final String host,
                          final int port) {
    this.connectionFactory = new ConnectionFactory();

    //Define parameters of factory.
    this.connectionFactory.setUsername(user);
    this.connectionFactory.setPassword(password);
    this.connectionFactory.setVirtualHost(virtualHost);
    this.connectionFactory.setHost(host);
    this.connectionFactory.setPort(port);
  }

  @Override
  public @NotNull Connection connection() throws RabbitClientAbsentException {
    //create new if absent
    if (this.connection == null || !this.connection.isOpen()) {
      //Create
      try {
        this.connection = this.connectionFactory.newConnection();
      } catch (final Exception exception) {
        //Error with factory.
        throw new RabbitClientAbsentException(this.connectionFactory);
      }
    }
    return this.connection;
  }

  @Override
  public @NotNull Channel channel() throws RabbitClientAbsentException {
    //Create channel if absent.
    if (this.channel == null || !this.channel.isOpen()) {
      final Connection connection = this.connection();
      //Create channel
      try {
        this.channel = connection.createChannel();
      } catch (final Exception exception) {
        //Error with factory.
        throw new RabbitClientAbsentException(this.connectionFactory);
      }
    }
    return this.channel;
  }

  @Override
  public @NotNull Channel newChannel() throws RabbitClientAbsentException {
    try {
      return this.connection().createChannel();
    } catch (final Exception exception) {
      //Error with factory.
      throw new RabbitClientAbsentException(this.connectionFactory);
    }
  }

  /**
   * See {@link IRabbitConnection#newPublisher()}
   */
  @Override
  public @NotNull IRabbitPublisher newPublisher() {
    //Create publisher without ttl.
    return new RabbitPublisher(this, null);
  }

  @Override
  public @NotNull IRabbitSubscriber newSubscriber(@Nullable final String exchange,
                                                  @Nullable final String routingKey) throws RabbitClientAbsentException {
    return new RabbitSubscriber(this, exchange, routingKey);
  }

  @Override
  public @NotNull IRabbitConnection createQueueIfAbsent(@Nullable String name,
                                                        boolean durable,
                                                        boolean exclusive,
                                                        boolean autoDelete,
                                                        @Nullable Map<String, Object> arguments) throws RabbitIOException, RabbitClientAbsentException {
    //Null check
    Objects.requireNonNull(name);

    //Pass to rabbitmq.
    try {
      //Declare queue
      this.channel().queueDeclare(name, durable, exclusive, autoDelete, arguments);

      //Log
      log.info("The queue={} is now available.", name);
    } catch (final IOException exception) {
      //Pass error
      throw new RabbitIOException(exception);
    }

    return this;
  }

  @Override
  public @NotNull IRabbitConnection createExchangeIfAbsent(@Nullable String name,
                                                           @Nullable String type) throws RabbitIOException, RabbitClientAbsentException {
    //Null check
    Objects.requireNonNull(name);
    Objects.requireNonNull(type);

    try {
      //Declare exchange
      this.channel().exchangeDeclare("icarus-broadcast", "fanout");
      //Log
      log.info("The exchange={} with the type={} is now available.", name, type);
    } catch (final IOException exception) {
      //Pass error
      throw new RabbitIOException(exception);
    }
    return this;
  }
}
