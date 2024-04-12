package dev.dotspace.network.library.connection;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

//Swagger
@Schema(implementation=ImmutableAddressName.class)
public interface IAddressName {
  /**
   * Get address.
   */
  //Swagger
  @Schema(example="127.0.0.1", description="Address of profile.")
  @NotNull String address();
}
