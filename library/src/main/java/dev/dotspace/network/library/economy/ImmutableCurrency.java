package dev.dotspace.network.library.economy;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


//Swagger
@Schema(
    name="Currency",
    description="Currency to store information."
)
public record ImmutableCurrency(@NotNull String name,
                                @NotNull String symbol,
                                @NotNull String display,
                                @Nullable String displayPlural
) implements ICurrency {
  /**
   * Convert {@link ICurrency} to {@link ImmutableCurrency}.
   *
   * @param currency to convert.
   * @return instance of {@link ICurrency}.
   */
  public static @NotNull ICurrency of(@Nullable final ICurrency currency) {
    //Null check
    Objects.requireNonNull(currency);

    return new ImmutableCurrency(currency.name(), currency.symbol(), currency.display(), currency.displayPlural());
  }
}
