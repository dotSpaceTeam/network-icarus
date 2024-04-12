package dev.dotspace.network.library.economy;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


//Swagger
@Schema(
    name="Balance",
    description="Balance to get value of all transactions."
)
public record ImmutableBalance(@NotNull ICurrency currency,
                               long balance
) implements IBalance {
  /**
   * Convert {@link IBalance} to {@link ImmutableBalance}.
   *
   * @param balance to convert.
   * @return instance of {@link IBalance}.
   */
  public static @NotNull IBalance of(@Nullable final IBalance balance) {
    //Null check
    Objects.requireNonNull(balance);

    return new ImmutableBalance(balance.currency(), balance.balance());
  }
}
