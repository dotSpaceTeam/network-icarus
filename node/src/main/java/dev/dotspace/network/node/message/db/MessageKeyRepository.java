package dev.dotspace.network.node.message.db;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Class to manipulate {@link MessageKeyEntity}.
 */
public interface MessageKeyRepository extends JpaRepository<MessageKeyEntity, Long> {
  /**
   * Check if key is present.
   *
   * @param key to check if present.
   * @return true, if present.
   */
  boolean existsByKey(@Nullable final String key);

  /**
   * Get {@link MessageKeyEntity} from key.
   *
   * @param key to look up.
   * @return currency as entity.
   */
  @NotNull Optional<MessageKeyEntity> findByKey(@Nullable final String key);
}
