package dev.dotspace.network.library.economy;


import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


//Swagger
@Schema(
    name="Transaction",
    description="Store balance change for profile."
)
public record ImmutableTransaction(@NotNull String currency,
                                   int amount,
                                   @NotNull TransactionType transactionType
) implements ITransaction {
  /**
   * Convert {@link ITransaction} to {@link ImmutableTransaction}.
   *
   * @param transaction to convert.
   * @return instance of {@link ImmutableTransaction}.
   */
  public static @NotNull ITransaction of(@Nullable final ITransaction transaction) {
    //Null check
    Objects.requireNonNull(transaction);

    return new ImmutableTransaction(transaction.currency(), transaction.amount(), transaction.transactionType());
  }
}
