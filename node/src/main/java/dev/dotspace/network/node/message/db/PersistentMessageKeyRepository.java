package dev.dotspace.network.node.message.db;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * Class to manipulate {@link PersistentMessageKeyEntity}.
 */
public interface PersistentMessageKeyRepository extends JpaRepository<PersistentMessageKeyEntity, Long> {
  /**
   * Check if key is present.
   *
   * @param key to check if present.
   * @return true, if present.
   */
  boolean existsByKey(@Nullable final String key);

  /**
   * Get {@link PersistentMessageKeyEntity} from key.
   *
   * @param key to look up.
   * @return currency as entity.
   */
  @NotNull Optional<PersistentMessageKeyEntity> findByKey(@Nullable final String key);
}
