package dev.dotspace.network.node.position.db;

import dev.dotspace.network.library.position.IViewPosition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;


@Entity
@Table(name="PositionView")
@NoArgsConstructor
@Accessors(fluent=true)
public final class ViewPositionElement implements IViewPosition {
  /**
   * Identity of element
   */
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Getter
  private Long id;

  @OneToOne
  @JoinColumn(name="Position", nullable=false, unique=true)
  private PositionElement position;

  @Column(name="Yaw", nullable=false)
  @Getter
  @Setter
  private double yaw;

  @Column(name="Pitch", nullable=false)
  @Getter
  @Setter
  private double pitch;

  public ViewPositionElement(@NotNull final PositionElement position,
                             final double yaw,
                             final double pitch) {
    this.position = position;
    this.yaw = yaw;
    this.pitch = pitch;
  }

  @Override
  public @NotNull String key() {
    return this.position.key();
  }

  @Override
  public double x() {
    return this.position.x();
  }

  @Override
  public double y() {
    return this.position.y();
  }

  @Override
  public double z() {
    return this.position.z();
  }
}
