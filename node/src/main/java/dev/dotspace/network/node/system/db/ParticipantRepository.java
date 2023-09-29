package dev.dotspace.network.node.system.db;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Queries to manipulate {@link ParticipantEntity}.
 */
public interface ParticipantRepository extends JpaRepository<ParticipantEntity, Long> {
  /**
   * Get {@link ParticipantEntity} from runtimeId.
   *
   * @param identifier get {@link ParticipantEntity} from runtimeId.
   * @return present {@link ParticipantEntity}.
   */
  @NotNull Optional<ParticipantEntity> findByIdentifier(@Nullable final String identifier);

  /**
   * Check if runtimeId is present.
   *
   * @param identifier to check.
   * @return true, if tuple present.
   */
  boolean existsByIdentifier(@Nullable final String identifier);

}
