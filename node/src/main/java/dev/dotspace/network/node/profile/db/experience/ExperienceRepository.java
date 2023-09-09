package dev.dotspace.network.node.profile.db.experience;

import dev.dotspace.network.node.profile.db.ProfileEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


/**
 * Queries to manipulate profiles.
 */
@Component
public interface ExperienceRepository extends JpaRepository<ExperienceEntity, Long> {
  @NotNull Optional<ExperienceEntity> findByProfileAndName(@NotNull final ProfileEntity profile,
                                                           @NotNull final String name);


  /**
   * Get playtime of all sessions for profile.
   */
  @Query(value="""
      SELECT
        SUM(Experience)
      FROM
        Profile_Experience
      WHERE
       Profile = :profile
      ;
      """, nativeQuery=true)
  @NotNull Optional<Long> getTotal(@Param("profile") final long profile);

  @NotNull List<ExperienceEntity> findByProfile(@NotNull final ProfileEntity profile);
}
