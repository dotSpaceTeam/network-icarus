package dev.dotspace.network.library.economy;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


//Swagger
@Schema(implementation=ImmutableCurrency.class)
public interface ICurrency {
  /**
   * Name of currency.
   *
   * @return currency name as {@link String}
   */
  //Swagger
  @Schema(example="coin", description="Name of coin to store object.")
  @NotNull String name();

  /**
   * Returns the symbol of the currency. It can be a symbol, word or abbreviation.
   *
   * @return currency symbol as {@link String}
   */
  //Swagger
  @Schema(example="$C", description="Symbol of coin.")
  @NotNull String symbol();

  /**
   * Returns the name of the currency.
   *
   * @return currency name as {@link String}
   */
  //Swagger
  @Schema(example="Coin", description="Name of the coin, if plural. (!= 1.0)")
  @NotNull String display();

  /**
   * Optional:
   * Name if coins are not equal 1.
   *
   * @return name of currency.
   */
  @Schema(example="Coins", description="Name of the coin, if it is the more number. (= 1.0)")
  @Nullable String displayPlural();
}
