package dev.dotspace.network.node.economy.db;

import dev.dotspace.network.node.exception.ElementNotPresentException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * Queries to manipulate currencies.
 */
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
  /**
   * Check if symbol is present.
   *
   * @param symbol to check if present.
   * @return true, if present.
   */
  boolean existsBySymbol(@Nullable final String symbol);

  /**
   * Get {@link CurrencyEntity} from symbol.
   *
   * @param symbol to look up.
   * @return currency as entity.
   */
  @NotNull Optional<CurrencyEntity> findBySymbol(@Nullable final String symbol);

  /**
   * Get {@link CurrencyEntity} from symbol.
   *
   * @param name to look up.
   * @return currency as entity.
   */
  @NotNull Optional<CurrencyEntity> findByName(@Nullable final String name);

  default @NotNull CurrencyEntity nameElseThrow(@Nullable final String name) throws ElementNotPresentException {
    return this
        //Get uniqueId
        .findByName(name)
        //Else throw error
        .orElseThrow(() -> new ElementNotPresentException(null, "No currency with name="+name+" present."));
  }
}
