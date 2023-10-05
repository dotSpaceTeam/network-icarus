package dev.dotspace.network.rabbitmq.subscriber;

import com.rabbitmq.client.Channel;
import dev.dotspace.common.function.ThrowableConsumer;
import dev.dotspace.network.rabbitmq.IRabbitClient;
import dev.dotspace.network.rabbitmq.RabbitField;
import dev.dotspace.network.rabbitmq.exception.RabbitClientAbsentException;
import dev.dotspace.network.rabbitmq.exception.RabbitIOException;
import dev.dotspace.network.rabbitmq.paritcipant.RabbitParticipant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


public final class RabbitSubscriber extends RabbitParticipant implements IRabbitSubscriber, AutoCloseable {
  /**
   * Channel to listen to subscription.
   */
  private final @NotNull Channel channel;

  private final @NotNull String queueName;

  /**
   * Create participant.
   *
   * @param rabbitClient to use participant.
   */
  public RabbitSubscriber(@Nullable final IRabbitClient rabbitClient,
                          @Nullable final String exchange,
                          @Nullable final String routingKey) throws RabbitClientAbsentException {
    super(rabbitClient);

    //Null check
    Objects.requireNonNull(exchange);
    Objects.requireNonNull(routingKey);

    //Create new channel
    this.channel = this.rabbitClient().newChannel();

    try {
      this.queueName = this.channel.queueDeclare().getQueue();
      this.channel.queueBind(this.queueName, exchange, routingKey);
    } catch (final Exception exception) {
      //Error with factory.
      throw new RabbitClientAbsentException(this.rabbitClient().connectionFactory());
    }
  }

  @Override
  public @NotNull IRabbitSubscriber subscribe(@Nullable String key,
                                              @Nullable ThrowableConsumer<byte[]> consumer) throws RabbitIOException {
    //Null check
    Objects.requireNonNull(key);
    Objects.requireNonNull(consumer);

    try {
      final String subscription = this.channel.basicConsume(this.queueName, false, (consumerTag, message) -> {
        try {
          consumer.accept(message.getBody());
        } catch (Throwable e) {
          throw new RuntimeException(e);
        }
      }, (consumerTag, sig) -> {

      });
    } catch (final IOException exception) {
      //Throw error.
      throw new RabbitIOException(exception);
    }

    return this;
  }

  @Override
  public @NotNull <TYPE> IRabbitSubscriber subscribe(@Nullable String key,
                                                     @Nullable Class<TYPE> typeClass,
                                                     @Nullable ThrowableConsumer<TYPE> consumer) throws RabbitIOException {
    //Null check
    Objects.requireNonNull(key);
    Objects.requireNonNull(consumer);


    try {
      final String subscription = this.channel.basicConsume(this.queueName, (consumerTag, message) -> {
        //Ignore if not json -> nothing to parse.
        if (!message.getProperties().getContentType().equals(RabbitField.APPLICATION_JSON)) {
          return;
        }

        //Get headers
        @Nullable final Map<String, Object> headers = message.getProperties().getHeaders();

        //Return if no headers present.
        if (headers == null || headers.isEmpty()) {
          return;
        }

        final boolean parsable = Optional.ofNullable(headers.get(RabbitField.MAPPER_CLASS)).map(String::valueOf)
            .map(s -> {
              try {
                return typeClass.equals(Class.forName(s));
              } catch (ClassNotFoundException e) {
              }
              return false;
            })
            .orElse(false);

        //Not parsable content -> return
        if (!parsable) {
          return;
        }

        //Map content.
        final TYPE type = this.mapper().readValue(message.getBody(), typeClass);

        try {
          consumer.accept(type);
        } catch (Throwable e) {
          throw new RuntimeException(e);
        }
      }, (consumerTag, sig) -> {
      });
    } catch (final IOException exception) {
      //Throw error.
      throw new RabbitIOException(exception);
    }
    return this;
  }

  @Override
  public void close() throws Exception {
    this.channel.close();
  }
}
