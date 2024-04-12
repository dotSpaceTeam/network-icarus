package dev.dotspace.network.node.profile.db;

import dev.dotspace.network.node.database.exception.EntityNotPresentException;
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
   * Get profile from name.
   *
   * @param name get profile from.
   * @return profile as entity.
   */
  @NotNull Optional<ProfileEntity> findByNameIgnoreCase(@Nullable final String name);

  /**
   * Check if uniqueId is present.
   *
   * @param uniqueId to check.
   * @return true, if tuple present.
   */
  boolean existsByUniqueId(@Nullable final String uniqueId);


  default @NotNull ProfileEntity profileElseThrow(@Nullable final String uniqueId,
                                                  @NotNull final String message) throws EntityNotPresentException {
    return this
        //Get uniqueId
        .findByUniqueId(uniqueId)
        //Else throw error with message
        .orElseThrow(() -> new EntityNotPresentException(null, message));
  }

  default @NotNull ProfileEntity profileElseThrow(@Nullable final String uniqueId) throws EntityNotPresentException {
    return this
        //Get uniqueId
        .findByUniqueId(uniqueId)
        //Else throw error
        .orElseThrow(() -> new EntityNotPresentException(null, "No profile with uniqueId="+uniqueId+" present."));
  }
}
