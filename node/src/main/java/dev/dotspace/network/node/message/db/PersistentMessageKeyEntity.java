package dev.dotspace.network.node.message.db;

import dev.dotspace.network.library.key.IKey;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;


@Entity
@Table(name="PersistentMessageKey")
@NoArgsConstructor
@Accessors(fluent=true)
public final class PersistentMessageKeyEntity implements IKey {
  /**
   * Identity of element
   */
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Getter
  private Long id;

  @Column(name="Key", nullable=false, unique=true)
  @Getter
  private String key;

  /**
   * Create instance of key.
   */
  public PersistentMessageKeyEntity(@NotNull final String key) {
    this.key = key;
  }
}
