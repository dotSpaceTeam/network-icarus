package dev.dotspace.network.node.economy.db;

import dev.dotspace.network.library.economy.ICurrency;
import dev.dotspace.network.library.economy.ImmutableCurrency;
import dev.dotspace.network.node.database.AbstractDatabase;
import dev.dotspace.network.node.database.exception.EntityNotPresentException;
import dev.dotspace.network.node.profile.db.ProfileRepository;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
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
   * Repository to manipulates profiles.
   */
  @Autowired
  private ProfileRepository profileRepository;

  public @NotNull List<ICurrency> getCurrencyList() {
    return this.currencyRepository.findAll()
        .stream()
        .map(ImmutableCurrency::of)
        .toList();
  }

  public @NotNull ICurrency createCurrency(@Nullable String name,
                                           @Nullable final String symbol,
                                           @Nullable final String display,
                                           @Nullable final String displayPlural) {
    //Null checks
    Objects.requireNonNull(name);
    Objects.requireNonNull(symbol);
    Objects.requireNonNull(display);

    //Set name of currency as lower
    name = name.toLowerCase(Locale.ROOT);

    @Nullable final CurrencyEntity currencyEntity = this.currencyRepository
        //Get from name
        .findByName(name)
        //Else null.
        .orElse(null);

    //edit
    if (currencyEntity != null) {
      currencyEntity
          //Update parameter
          .symbol(symbol)
          .display(display)
          .displayPlural(displayPlural);

      //Update db.
      return ImmutableCurrency.of(this.currencyRepository.save(currencyEntity));
    }

    //Create new
    return ImmutableCurrency.of(this.currencyRepository.save(new CurrencyEntity(name, symbol, display, displayPlural)));
  }

  public @NotNull ICurrency getCurrency(@Nullable String name) throws EntityNotPresentException {
    //Null checks
    Objects.requireNonNull(name);

    //lower
    name = name.toLowerCase(Locale.ROOT);

    //Get currency
    return ImmutableCurrency.of(this.currencyRepository.nameElseThrow(name));
  }
}
