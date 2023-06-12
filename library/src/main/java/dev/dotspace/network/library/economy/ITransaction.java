package dev.dotspace.network.library.economy;

import org.jetbrains.annotations.NotNull;

public interface ITransaction {
  /**
   * Get amount of balanced moved within transaction.
   *
   * @return amount as int.
   */
  int amount();

  /**
   * Type of transaction.
   *
   * @return type of transaction.
   */
  @NotNull TransactionType transactionType();
}
