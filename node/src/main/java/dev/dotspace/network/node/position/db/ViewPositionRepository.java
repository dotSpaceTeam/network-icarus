package dev.dotspace.network.node.position.db;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ViewPositionRepository extends JpaRepository<ViewPositionElement, Long> {

  /**
   * Get view.
   *
   * @param positionElement to get view element from.
   * @return get {@link ViewPositionElement}.
   */
  @NotNull Optional<ViewPositionElement> findByPosition(@NotNull final PositionElement positionElement);
}
