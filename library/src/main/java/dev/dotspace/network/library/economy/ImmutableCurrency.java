package dev.dotspace.network.library.economy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record ImmutableCurrency(@NotNull String symbol,
                                @NotNull String name,
                                @Nullable String pluralName) implements ICurrency {

  /**
   * Convert {@link ICurrency} to {@link ImmutableCurrency}.
   *
   * @param currency to convert.
   * @return instance of {@link ICurrency}.
   */
  public static @NotNull ICurrency of(@Nullable final ICurrency currency) {
    //Null check
    Objects.requireNonNull(currency);

    return new ImmutableCurrency(currency.symbol(), currency.name(), currency.pluralName());
  }
}
