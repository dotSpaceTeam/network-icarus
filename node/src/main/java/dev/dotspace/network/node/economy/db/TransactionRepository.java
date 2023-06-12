package dev.dotspace.network.node.economy.db;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Queries to manipulate currencies.
 */
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
