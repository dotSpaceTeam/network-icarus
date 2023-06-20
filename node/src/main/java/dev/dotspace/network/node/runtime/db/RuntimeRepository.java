package dev.dotspace.network.node.runtime.db;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Queries to manipulate {@link RuntimeEntity}.
 */
public interface RuntimeRepository extends JpaRepository<RuntimeEntity, Long> {
  /**
   * Get {@link RuntimeEntity} from runtimeId.
   *
   * @param runtimeId get {@link RuntimeEntity} from runtimeId.
   * @return present {@link RuntimeEntity}.
   */
  @NotNull Optional<RuntimeEntity> findByRuntimeId(@Nullable final String runtimeId);

  /**
   * Check if runtimeId is present.
   *
   * @param runtimeId to check.
   * @return true, if tuple present.
   */
  boolean existsByRuntimeId(@Nullable final String runtimeId);
}
