package dev.dotspace.network.node.economy.db;

import dev.dotspace.common.SpaceLibrary;
import dev.dotspace.common.response.Response;
import dev.dotspace.network.library.economy.ICurrency;
import dev.dotspace.network.library.economy.ITransaction;
import dev.dotspace.network.library.economy.ImmutableCurrency;
import dev.dotspace.network.library.economy.ImmutableTransaction;
import dev.dotspace.network.library.economy.TransactionType;
import dev.dotspace.network.node.database.AbstractDatabase;
import dev.dotspace.network.node.profile.db.ProfileEntity;
import dev.dotspace.network.node.profile.db.ProfileRepository;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("economyDatabase")
@Log4j2
public final class EconomyDatabase extends AbstractDatabase implements IEconomyDatabase {
  /**
   * Instance to communicate tp profiles.
   */
  @Autowired
  private CurrencyRepository currencyRepository;

  /**
   * Repository to manipulates transaction.
   */
  @Autowired
  private TransactionRepository transactionRepository;

  /**
   * Repository to manipulates profiles.
   */
  @Autowired
  private ProfileRepository profileRepository;

  @Override
  public @NotNull Response<ICurrency> createCurrency(@Nullable String symbol,
                                                     @Nullable String name,
                                                     @Nullable String pluralName) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null checks
      Objects.requireNonNull(symbol);
      Objects.requireNonNull(name);

      /*
       * Check if symbol is already existing
       */
      if (this.currencyRepository.existsBySymbol(symbol)) {
        log.info("Currency with symbol='{}', already present.", symbol);
        return null;
      }

      return ImmutableCurrency.of(this.currencyRepository.save(new CurrencyEntity(symbol, name, pluralName)));
    });
  }

  @Override
  public @NotNull Response<ITransaction> createTransaction(@Nullable String uniqueId,
                                                                      @Nullable String symbol,
                                                                      int amount,
                                                                      @Nullable TransactionType transactionType) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(symbol);
      Objects.requireNonNull(transactionType);

      //Get profile of transaction
      final ProfileEntity profile = this.profileRepository
        .findByUniqueId(uniqueId)
        .orElseThrow(this.failOptional("No profile with uniqueId='%s' found to set transaction for.".formatted(uniqueId)));

      //Get currency
      final CurrencyEntity currency = this.currencyRepository
        .findBySymbol(symbol)
        .orElseThrow(this.failOptional("No currency with symbol='%s' found to set transaction for.".formatted(symbol)));

      //Convert amount to positive if deposited, negative if withdrawn.
      final int transactionAmount = Math.abs(amount) * (transactionType == TransactionType.WITHDRAW ? -1 : 1);
      log.debug("Converted transaction amount from={} to={}.", amount, transactionAmount);

      return ImmutableTransaction
        .of(this.transactionRepository.save(new TransactionEntity(profile, currency, transactionAmount, transactionType)));
    });
  }
}
