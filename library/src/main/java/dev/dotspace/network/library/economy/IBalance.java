package dev.dotspace.network.library.economy;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;


//Swagger
@Schema(implementation=ImmutableBalance.class)
public interface IBalance {
  /**
   * Type of {@link ICurrency} balanced is stored.
   *
   * @return value of currency.
   */
  //Swagger
  @Schema(description="Name of currency for balance.")
  @NotNull ICurrency currency();

  /**
   * Amount of balance.
   *
   * @return value of balance.
   */
  @Schema(example="60", description="Value of balance for currency.")
  long balance();
}
