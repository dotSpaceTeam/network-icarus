package dev.dotspace.network.node.message_old.db;


import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Class to manipulate {@link MessageEntity}.
 */
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
  /**
   * Get value from key. (Key is sql key (message_key_id, locale))
   *
   * @param key    to search for message references.
   * @param locale to value.
   * @return information of key, wrapped in {@link Optional}.
   */
  @NotNull Optional<MessageEntity> findByKeyAndLocale(@NotNull final MessageKeyEntity key,
                                                      @NotNull final String locale);

  /**
   * Get all entries.
   *
   * @param key to search for message references.
   * @return all information of key.
   */
  @NotNull List<MessageEntity> findByKey(@NotNull final MessageKeyEntity key);
}
