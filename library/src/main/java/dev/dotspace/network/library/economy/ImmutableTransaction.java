package dev.dotspace.network.library.economy;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record ImmutableTransaction(int amount,
                                   @NotNull TransactionType transactionType) implements ITransaction {
  /**
   * Convert {@link ITransaction} to {@link ImmutableTransaction}.
   *
   * @param profileAttribute to convert.
   * @return instance of {@link ImmutableTransaction}.
   */
  public static @NotNull ITransaction of(@Nullable final ITransaction profileAttribute) {
    //Null check
    Objects.requireNonNull(profileAttribute);

    return new ImmutableTransaction(profileAttribute.amount(), profileAttribute.transactionType());
  }
}
