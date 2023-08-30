package dev.dotspace.network.library.economy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ICurrency {
  /**
   * Returns the symbol of the currency. It can be a symbol, word or abbreviation.
   *
   * @return currency symbol as {@link String}
   */
  @NotNull String symbol();

  /**
   * Returns the name of the currency.
   *
   * @return currency name as {@link String}
   */
  @NotNull String name();

  /**
   * Optional:
   * Name if coins are not equal 1.
   *
   * @return name of currency.
   */
  @Nullable String pluralName();
}
