package dev.dotspace.network.client.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import dev.dotspace.network.client.rabbitmq.exception.RabbitClientAbsentException;
import dev.dotspace.network.client.rabbitmq.exception.RabbitException;
import dev.dotspace.network.client.rabbitmq.exception.RabbitIOException;
import dev.dotspace.network.client.rabbitmq.publisher.IRabbitPublisher;
import dev.dotspace.network.client.rabbitmq.publisher.RabbitPublisher;
import dev.dotspace.network.client.rabbitmq.subscriber.IRabbitSubscriber;
import dev.dotspace.network.client.rabbitmq.subscriber.RabbitSubscriber;
import dev.dotspace.network.library.exception.ClientNotActiveException;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Log4j2
@Accessors(fluent=true)
public final class RabbitClient implements IRabbitClient {
  @Getter
  private final @NotNull ConnectionFactory connectionFactory;
  private @Nullable Connection connection;
  private @Nullable Channel channel;

  /**
   * Create with paramerts.
   */
  public RabbitClient(@Nullable final String user,
                      @Nullable final String password,
                      @Nullable final String virtualHost,
                      @Nullable final String host,
                      final int port) {
    //Create base factory ->
    this();

    //Null check
    Objects.requireNonNull(user);
    Objects.requireNonNull(password);
    Objects.requireNonNull(virtualHost);
    Objects.requireNonNull(host);

    //Define parameters of factory.
    this.connectionFactory.setUsername(user);
    this.connectionFactory.setPassword(password);
    this.connectionFactory.setVirtualHost(virtualHost);
    this.connectionFactory.setHost(host);
    this.connectionFactory.setPort(port);
  }

  /**
   * Create with uri
   */
  public RabbitClient(@Nullable final String uri) throws Exception {
    //Create base factory ->
    this();

    //Null check
    Objects.requireNonNull(uri);

    //Define parameters of factory.
    this.connectionFactory.setUri(uri);
  }

  private RabbitClient() {
    //Create base factory
    this.connectionFactory = new ConnectionFactory();
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
   * See {@link IRabbitClient#newPublisher()}
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
  public @NotNull IRabbitClient createQueueIfAbsent(@Nullable String name,
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
  public @NotNull IRabbitClient createExchangeIfAbsent(@Nullable String name,
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

  //static
  private final static @NotNull String EXCHANGE_NAME = "icarus_fanout_exchange";
  private final static @NotNull String QUEUE_NAME = "icarus_data_update";
  private static @Nullable RabbitClient client;
  //Check if client is enabled.

  /**
   * Name of exchange.
   */
  public static @NotNull String fanoutExchange() {
    return EXCHANGE_NAME;
  }

  /**
   * Get client instance.
   *
   * @return get singleton instance.
   */
  public static @NotNull IRabbitClient client() {
    return Optional
        //Get client
        .ofNullable(client)
        //Else error
        .orElseThrow(ClientNotActiveException::new);
  }

  /**
   * Enable if not enabled.
   * -> see here for uri -> <a href="https://www.rabbitmq.com/uri-spec.html">https://www.rabbitmq.com/uri-spec.html</a>
   */
  public static void connect(@Nullable final String rabbitUri) {
    //Null check
    Objects.requireNonNull(rabbitUri);

    //Already enabled.
    if (present()) {
      log.warn("Client already enabled.");
      return;
    }

    //Init client
    try {
      buildClient(new RabbitClient(rabbitUri));
    } catch (final Exception exception) {
      exception.printStackTrace();
    }
    log.info("Enabled client.");
  }

  private static void buildClient(@NotNull final RabbitClient rabbitClient) throws RabbitException {
    client = rabbitClient;

    //Create client
    rabbitClient.createQueueIfAbsent(QUEUE_NAME, true, true, false, null);
    //Create exchange
    rabbitClient.createExchangeIfAbsent(EXCHANGE_NAME, "fanout");
  }


  public static void disconnect() {
    //Ignore if client is not present.
    if (client == null) {
      return;
    }

    //Set client to null.
    client = null;

    log.info("Disabled client.");
  }

  public static boolean present() {
    return client != null;
  }

  /**
   * Check if client is connected.
   */
  public static boolean connected() {
    return client != null;
  }

  /**
   * Check if client is disconnected, only so if enabled and last connections failed.
   */
  public static boolean disconnected() {
    return client == null;

  }
}
