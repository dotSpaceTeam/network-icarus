package dev.dotspace.network.node.prohibit.db;

import dev.dotspace.network.node.database.exception.EntityNotPresentException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * Queries to manipulate profiles.
 */
public interface ProhibitReasonRepository extends JpaRepository<ProhibitReasonEntity, Long> {
  /**
   * Get prohibit reason from name.
   *
   * @param name get profile from.
   * @return profile as entity.
   */
  @NotNull Optional<ProhibitReasonEntity> findByNameIgnoreCase(@Nullable final String name);

  /**
   * Check if reason is present.
   *
   * @param name to check.
   * @return true, if tuple present.
   */
  boolean existsByName(@Nullable final String name);

  default @NotNull ProhibitReasonEntity reasonElseThrow(@Nullable final String name) throws EntityNotPresentException {
    return this
        //Get uniqueId
        .findByNameIgnoreCase(name)
        //Else throw error
        .orElseThrow(() -> new EntityNotPresentException(null, "No prohibitReason with name="+name+" present."));
  }
}
