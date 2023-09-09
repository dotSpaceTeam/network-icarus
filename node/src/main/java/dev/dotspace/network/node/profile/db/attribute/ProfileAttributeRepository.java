package dev.dotspace.network.node.profile.db.attribute;

import dev.dotspace.network.node.profile.db.ProfileEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Queries to manipulate attributes.
 */
public interface ProfileAttributeRepository extends JpaRepository<ProfileAttributeEntity, Long> {
  /**
   * Get value from key. (Key is sql key (profile_id, key))
   *
   * @param profile to search for profile references.
   * @param key to value.
   * @return information of key, wrapped in {@link Optional}.
   */
  @NotNull Optional<ProfileAttributeEntity> findByProfileAndKey(@NotNull final ProfileEntity profile,
                                                                @NotNull final String key);

  /**
   * Get all entries.
   *
   * @param profile to search for profile references.
   * @return all information of profile
   */
  @NotNull List<ProfileAttributeEntity> findByProfile(@NotNull final ProfileEntity profile);
}
