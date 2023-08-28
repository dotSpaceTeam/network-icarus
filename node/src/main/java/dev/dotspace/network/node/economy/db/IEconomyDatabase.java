package dev.dotspace.network.node.economy.db;

import dev.dotspace.common.response.Response;
import dev.dotspace.network.library.economy.ICurrency;
import dev.dotspace.network.library.economy.ITransaction;
import dev.dotspace.network.library.economy.TransactionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IEconomyDatabase {

  @NotNull Response<ICurrency> createCurrency(@Nullable final String symbol,
                                              @Nullable final String name,
                                              @Nullable final String pluralName);

  @NotNull Response<ITransaction> createTransaction(@Nullable final String uniqueId,
                                                    @Nullable final String symbol,
                                                    final int amount,
                                                    @Nullable final TransactionType transactionType);

}
