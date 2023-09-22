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
  private long x;
  @Column(name = "y", nullable = false)
  @Setter
  private long y;

  @Column(name = "z", nullable = false)
  @Setter
  private long z;

  public PositionElement(final String key,
                         final long x,
                         final long y,
                         final long z) {
    this.key = key;
    this.x = x;
    this.y = y;
    this.z = z;
  }
}
