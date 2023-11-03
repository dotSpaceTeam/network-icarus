package dev.dotspace.network.rabbitmq.subscriber;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import dev.dotspace.common.function.ThrowableConsumer;
import dev.dotspace.network.rabbitmq.IRabbitClient;
import dev.dotspace.network.rabbitmq.RabbitField;
import dev.dotspace.network.rabbitmq.exception.RabbitClientAbsentException;
import dev.dotspace.network.rabbitmq.exception.RabbitIOException;
import dev.dotspace.network.rabbitmq.paritcipant.RabbitParticipant;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Log4j2
public final class RabbitSubscriber extends RabbitParticipant implements IRabbitSubscriber, AutoCloseable {
  /**
   * Channel to listen to subscription.
   */
  private final @NotNull Channel channel;
  /**
   * Local name of this subscriber -> combination of queue and exchange.
   */
  private final @NotNull String queueName;

  /**
   * List of all subscriptions for content.
   */
  private final @NotNull List<ContentPayload> contentPayloadList;

  private final @NotNull List<ObjectPayload<?>> objectPayloadList;

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

    //Define content lists.
    this.contentPayloadList = new ArrayList<>();
    this.objectPayloadList = new ArrayList<>();

    //Create new channel
    this.channel = this.rabbitClient().newChannel();

    try {
      //Create queue.
      this.queueName = this.channel.queueDeclare().getQueue();
      //Bind queue to exchange and key.
      this.channel.queueBind(this.queueName, exchange, routingKey);

      //Subscriber
      this.directSubscribe((consumerTag, message) -> {
        //Loop trough every content payload and accept content.
        for (final ContentPayload contentPayload : this.contentPayloadList) {
          //Pass body.
          try {
            //Accept headers.
            contentPayload.consumer()
                .accept(new ImmutablePayload<>(message.getBody(), validateHeaders(message.getProperties().getHeaders())));
          } catch (final Throwable throwable) {
            //Error while consumer
            log.warn("Error while supplying payload message.", throwable);
          }
        }

        //Ignore if empty -> safe parse performance.
        if (this.objectPayloadList.isEmpty()) {
          return;
        }

        //Get mapper class from header
        @Nullable final Class<?> mapperClass = this.mapperClassFromDelivery(message).orElse(null);

        //Ignore if mapper class is null.
        if (mapperClass == null) {
          return;
        }

        //Build object
        final Object object = this.mapper().readValue(message.getBody(), mapperClass);

        //Loop trough every payload
        for (final ObjectPayload<?> objectPayload : this.objectPayloadList) {
          //If mapper class does not match payload class, jump to next.
          if (!objectPayload.typeClass().equals(mapperClass)) {
            continue;
          }

          try {
            //Accept object content.
            objectPayload.accept(object, message.getProperties().getHeaders());
          } catch (final Throwable throwable) {
            //Error while consumer
            log.warn("Error while supplying payload object.", throwable);
          }
        }

      });

    } catch (final Exception exception) {
      //Error with factory.
      throw new RabbitClientAbsentException(this.rabbitClient().connectionFactory());
    }
  }

  @Override
  public @NotNull IRabbitSubscriber subscribe(@Nullable ThrowableConsumer<IPayload<byte[]>> consumer) {
    //Null check
    Objects.requireNonNull(consumer);

    //Add to list
    this.contentPayloadList.add(new ContentPayload(consumer));

    return this;
  }

  @Override
  public @NotNull <TYPE> IRabbitSubscriber subscribe(@Nullable Class<TYPE> typeClass,
                                                     @Nullable ThrowableConsumer<IPayload<TYPE>> consumer) {
    //Null check
    Objects.requireNonNull(typeClass);
    Objects.requireNonNull(consumer);

    //Add handle
    this.objectPayloadList.add(new ObjectPayload<>(typeClass, consumer));

    return this;
  }

  private void directSubscribe(@Nullable final DeliverCallback deliverCallback) throws RabbitIOException {
    try {
      final String subscription = this.channel.basicConsume(this.queueName, deliverCallback, (consumerTag, sig) -> {
      });

      //Info.
      log.info("Subscribed to queue={}, subscription={}.", this.queueName, subscription);
    } catch (final IOException exception) {
      //Throw error.
      throw new RabbitIOException(exception);
    }
  }

  private @NotNull Optional<Class<?>> mapperClassFromDelivery(@NotNull final Delivery delivery) {
    //Get headers
    @Nullable final Map<String, Object> headers = delivery.getProperties().getHeaders();

    //Return if no headers present.
    if (headers == null || headers.isEmpty()) {
      return Optional.empty();
    }

    return Optional
        //Get value of class.
        .ofNullable(headers.get(RabbitField.MAPPER_CLASS))
        //Map object to string.
        .map(String::valueOf)
        //Get class and compare to typeClass.
        .map(s -> {
          try {
            //Get class from name
            return Class.forName(s);
            //Error
          } catch (final ClassNotFoundException exception) {
            //Print missing class
            log.warn("Class={} not present in repository(dependencies).", s);
          }
          //No match.
          return null;
        });
  }

  @Override
  public void close() throws Exception {
    this.channel.close();
  }

  //static and classes

  /**
   * Validate headers.
   */
  private static @NotNull Map<String, Object> validateHeaders(@Nullable final Map<String, Object> headers) {
    return headers != null ? headers : new HashMap<>();
  }

  public record ContentPayload(@NotNull ThrowableConsumer<IPayload<byte[]>> consumer
  ) {
  }

  public record ObjectPayload<TYPE>(@NotNull Class<TYPE> typeClass,
                                    @NotNull ThrowableConsumer<IPayload<TYPE>> consumer
  ) {

    /**
     * Pass an object -> cast, also pass error
     */
    @SuppressWarnings("unchecked")
    public void accept(@NotNull final Object object,
                       @Nullable final Map<String, Object> headers) throws Throwable {
      //Create and accept header.
      this.consumer.accept(new ImmutablePayload<>((TYPE) object, validateHeaders(headers)));
    }

  }
}
