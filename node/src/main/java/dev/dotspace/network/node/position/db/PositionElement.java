package dev.dotspace.network.node.position.db;

import dev.dotspace.network.library.position.IPosition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;


@Entity
@Table(name = "Position")
@Getter
@NoArgsConstructor
@Accessors(fluent = true)
public final class PositionElement implements IPosition {
  /**
   * Identity of element
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "Key", nullable = false, unique = true)
  private String key;

  @Column(name = "x", nullable = false)
  @Setter
  private double x;
  @Column(name = "y", nullable = false)
  @Setter
  private double y;

  @Column(name = "z", nullable = false)
  @Setter
  private double z;

  public PositionElement(final String key,
                         final double x,
                         final double y,
                         final double z) {
    this.key = key;
    this.x = x;
    this.y = y;
    this.z = z;
  }
}
