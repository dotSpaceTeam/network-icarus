package dev.dotspace.network.node.economy.db;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


/**
 * Queries to manipulate currencies.
 */
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
  /**
   * Get playtime of all sessions for profile.
   */
  @Query(value="""
      SELECT
        SUM(Amount) AS Balance
      FROM
        Transaction
      WHERE
        Currency = :currency
      AND
        Profile= :profile
      GROUP BY
        Currency
      ;
      """, nativeQuery=true)
  @NotNull Optional<Long> calculateBalance(@Param("currency") final long currency,
                                           @Param("profile") final long profile);
}
