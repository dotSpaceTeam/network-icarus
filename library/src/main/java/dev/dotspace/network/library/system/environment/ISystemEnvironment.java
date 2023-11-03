package dev.dotspace.network.library.system.environment;

import dev.dotspace.network.library.profile.ImmutableProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;


//Swagger
@Schema(implementation=ImmutableSystemEnvironment.class)
public interface ISystemEnvironment {
  /**
   * Get version of peered node.
   *
   * @return version of node.
   */
  //Swagger
  @Schema(example="1.0.0", description="Version of node.")
  @NotNull String nodeVersion();

  /**
   * Get valid url to connect to rabbitmq.
   *
   * @return uri to rabbit.
   */
  //Swagger
  @Schema(example="amqp://user:password@localhost:5672/icarus",
      description="Uri to connect to rabbit mq. -> Value will be encrypted")
  @NotNull String rabbitMq();
}
