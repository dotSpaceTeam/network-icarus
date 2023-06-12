package dev.dotspace.network.node.economy.db;

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
}
