package dev.dotspace.network.node.message.db;


import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


/**
 * Class to manipulate {@link PersistentMessageEntity}.
 */
public interface PersistentMessageRepository extends JpaRepository<PersistentMessageEntity, Long> {
  /**
   * Get value from key. (Key is sql key (message_key_id, locale))
   *
   * @param key    to search for message references.
   * @param locale to value.
   * @return information of key, wrapped in {@link Optional}.
   */
  @NotNull Optional<PersistentMessageEntity> findByKeyAndLocale(@NotNull final PersistentMessageKeyEntity key,
                                                                @NotNull final String locale);

  /**
   * Get all entries.
   *
   * @param key to search for message references.
   * @return all information of key.
   */
  @NotNull List<PersistentMessageEntity> findByKey(@NotNull final PersistentMessageKeyEntity key);

  /**
   * Get playtime of all sessions for profile.
   */
  @Query(value="""
      SELECT DISTINCT
        locale
      FROM
        Persistent_Message
      ;
      """, nativeQuery=true)
  @NotNull List<String> getLocales();

}
