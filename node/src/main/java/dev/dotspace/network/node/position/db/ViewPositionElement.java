package dev.dotspace.network.node.position.db;

import dev.dotspace.network.library.position.IViewPosition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "Position_View")
@NoArgsConstructor
@Accessors(fluent = true)
public final class ViewPositionElement implements IViewPosition {
  /**
   * Identity of element
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  private Long id;

  @OneToOne
  @JoinColumn(name = "Position", nullable = false, unique = true)
  private PositionElement position;

  @Column(name = "Yaw", nullable = false)
  @Getter
  @Setter
  private long yaw;

  @Column(name = "Pitch", nullable = false)
  @Getter
  @Setter
  private long pitch;

  public ViewPositionElement(@NotNull final PositionElement position,
                             final long yaw,
                             final long pitch) {
    this.position = position;
    this.yaw = yaw;
    this.pitch = pitch;
  }

  @Override
  public @NotNull String key() {
    return this.position.key();
  }

  @Override
  public long x() {
    return this.position.x();
  }

  @Override
  public long y() {
    return this.position.y();
  }

  @Override
  public long z() {
    return this.position.z();
  }
}
