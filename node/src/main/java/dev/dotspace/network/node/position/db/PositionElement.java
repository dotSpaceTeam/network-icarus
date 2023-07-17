package dev.dotspace.network.node.position.db;

import dev.dotspace.network.library.position.IPosition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Table(name = "Position")
@NoArgsConstructor
@Accessors(fluent = true)
public final class PositionElement implements IPosition {
  /**
   * Identity of element
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  private Long id;

  @Column(name = "Key", nullable = false, unique = true)
  @Getter
  private String key;

  @Column(name = "x", nullable = false)
  @Getter
  @Setter
  private long x;
  @Column(name = "y", nullable = false)
  @Getter
  @Setter
  private long y;

  @Column(name = "z", nullable = false)
  @Getter
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
