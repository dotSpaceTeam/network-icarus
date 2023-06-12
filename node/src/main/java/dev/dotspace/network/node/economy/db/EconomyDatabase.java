package dev.dotspace.network.node.economy.db;

import dev.dotspace.common.SpaceLibrary;
import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.network.library.economy.*;
import dev.dotspace.network.node.profile.db.ProfileEntity;
import dev.dotspace.network.node.profile.db.ProfileRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("economyDatabase")
public final class EconomyDatabase implements IEconomyDatabase {
  /**
   * Logger
   */
  private final static @NotNull Logger LOGGER = LogManager.getLogger(EconomyDatabase.class);

  /**
   * Instance to communicate tp profiles.
   */
  @Autowired
  private CurrencyRepository currencyRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  @Autowired
  private ProfileRepository profileRepository;


  @Override
  public @NotNull CompletableResponse<ICurrency> createCurrency(@Nullable String symbol,
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
        LOGGER.info("Currency with symbol='{}', already present.", symbol);
        return null;
      }

      return ImmutableCurrency.of(this.currencyRepository.save(new CurrencyEntity(symbol, name, pluralName)));
    });
  }

  @Override
  public @NotNull CompletableResponse<ITransaction> createTransaction(@Nullable String uniqueId,
                                                                      @Nullable String symbol,
                                                                      int amount,
                                                                      @Nullable TransactionType transactionType) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(symbol);
      Objects.requireNonNull(transactionType);

      final ProfileEntity profile = this.profileRepository
        .findByUniqueId(uniqueId)
        .orElseThrow(() -> {
          LOGGER.error("No profile with uniqueId='{}' found to set transaction for.", uniqueId);
          return new NullPointerException();
        });

      final CurrencyEntity currency = this.currencyRepository
        .findBySymbol(symbol)
        .orElseThrow(() -> {
          LOGGER.error("No currency with symbol='{}' found to set transaction for.", symbol);
          return new NullPointerException();
        });

      int transactionAmount = Math.abs(amount) * (transactionType == TransactionType.WITHDRAW ? -1 : 1);

      return ImmutableTransaction
        .of(this.transactionRepository.save(new TransactionEntity(profile, currency, transactionAmount, transactionType.id())));

    });
  }

}
