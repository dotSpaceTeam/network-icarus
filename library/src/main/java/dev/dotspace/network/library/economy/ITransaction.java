package dev.dotspace.network.library.economy;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;


//Swagger
@Schema(implementation=ImmutableTransaction.class)
public interface ITransaction {
  /**
   * Currency of transaction.
   *
   * @return currency for transaction.
   */
  //Swagger
  @Schema(example="coin", description="Name of currency to book transaction.")
  @NotNull String currency();

  /**
   * Get amount of balanced moved within transaction.
   *
   * @return amount as int.
   */
  //Swagger
  @Schema(example="1", description="Value of transaction.")
  int amount();

  /**
   * Type of transaction.
   *
   * @return type of transaction.
   */
  @Schema(example="DEPOSIT", description="Type of transaction.")
  @NotNull TransactionType transactionType();
}
