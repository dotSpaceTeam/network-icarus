package dev.dotspace.network.node.session.db;

import dev.dotspace.network.node.profile.db.ProfileEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Queries for session.
 */
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {
  /**
   * Get all entries of session.
   *
   * @param profile to search for profile references.
   * @return all information of profile.
   */
  @NotNull List<SessionEntity> findByProfile(@NotNull final ProfileEntity profile);

  /**
   * Get playtime of all sessions for profile.
   */
  @Query(value = """
    SELECT
      SUM(DATE_PART('ms', end_date - start_date)) AS duration
    FROM
    	Session
    WHERE
    	end_date IS NOT NULL
    AND
    	Profile = :profile
    ;
    """, nativeQuery = true)
  @NotNull Optional<Long> calculatePlaytime(@Param("profile") final long profile);
}
