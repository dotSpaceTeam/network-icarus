package dev.dotspace.network.node.economy.db;

import dev.dotspace.network.library.economy.ICurrency;
import dev.dotspace.network.library.economy.ITransaction;
import dev.dotspace.network.library.economy.ImmutableCurrency;
import dev.dotspace.network.library.economy.ImmutableTransaction;
import dev.dotspace.network.library.economy.TransactionType;
import dev.dotspace.network.node.database.AbstractDatabase;
import dev.dotspace.network.node.exception.ElementException;
import dev.dotspace.network.node.exception.ElementNotPresentException;
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
public final class EconomyDatabase extends AbstractDatabase {
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


  public @NotNull ICurrency createCurrency(@Nullable final String symbol,
                                           @Nullable final String name,
                                           @Nullable final String pluralName) throws ElementNotPresentException {
    //Null checks
    Objects.requireNonNull(symbol);
    Objects.requireNonNull(name);

    //Check if symbol is already existing
    if (this.currencyRepository.existsBySymbol(symbol)) {
      throw new ElementNotPresentException(null, "Currency with symbol="+symbol+", already present.");
    }

    //Create new
    return ImmutableCurrency.of(this.currencyRepository.save(new CurrencyEntity(symbol, name, pluralName)));
  }

  public @NotNull ITransaction createTransaction(@Nullable final String uniqueId,
                                                 @Nullable final String symbol,
                                                 final int amount,
                                                 @Nullable final TransactionType transactionType) throws ElementException {
    //Null check
    Objects.requireNonNull(uniqueId);
    Objects.requireNonNull(symbol);
    Objects.requireNonNull(transactionType);

    //Get profile of transaction
    final ProfileEntity profile = this.profileRepository.profileElseThrow(uniqueId);

    //Get currency
    final CurrencyEntity currency = this.currencyRepository
        .findBySymbol(symbol)
        .orElseThrow(() ->
            new ElementNotPresentException(null, "No currency with symbol="+symbol+" found to set transaction for."));

    //Convert amount to positive if deposited, negative if withdrawn.
    final int transactionAmount = Math.abs(amount)*(transactionType == TransactionType.WITHDRAW ? -1 : 1);
    log.debug("Converted transaction amount from={} to={}.", amount, transactionAmount);

    return ImmutableTransaction
        .of(this.transactionRepository.save(new TransactionEntity(profile, currency, transactionAmount, transactionType)));
  }
}
