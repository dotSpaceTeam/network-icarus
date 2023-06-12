package dev.dotspace.network.library.economy;

import org.jetbrains.annotations.NotNull;

public interface ITransaction {
  /**
   *
   * @return
   */
  int amount();

  /**
   *
   * @return
   */
  @NotNull TransactionType transactionType();
}
