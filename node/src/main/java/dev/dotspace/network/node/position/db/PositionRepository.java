package dev.dotspace.network.node.position.db;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Manipulate {@link PositionElement}.
 */
public interface PositionRepository extends JpaRepository<PositionElement, Long> {
  /**
   * Get value from key.
   *
   * @param key to search for message references.
   * @return information of key, wrapped in {@link Optional}.
   */
  @NotNull Optional<PositionElement> findByKey(@NotNull final String key);
}
