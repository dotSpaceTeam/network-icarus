package dev.dotspace.network.node.profile.db;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Queries to manipulate profiles.
 */
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
  /**
   * Get profile from uniqueId.
   *
   * @param uniqueId get profile from uniqueId.
   * @return profile as entity.
   */
  @NotNull Optional<ProfileEntity> findByUniqueId(@Nullable final String uniqueId);

  /**
   * Check if uniqueId is present.
   *
   * @param uniqueId to check.
   * @return true, if tuple present.
   */
  boolean existsByUniqueId(@Nullable final String uniqueId);
}
