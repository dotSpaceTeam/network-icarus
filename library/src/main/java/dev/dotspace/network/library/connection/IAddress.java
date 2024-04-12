package dev.dotspace.network.library.connection;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

//Swagger
@Schema(implementation=ImmutableAddress.class)
public interface IAddress extends IAddressName{
  /**
   * Get protocol version.
   */
  //Swagger
  @Schema(example="VERSION_4", description="Version of address.")
  @NotNull InternetProtocolVersion version();
}
