package dev.dotspace.network.node.message.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "Message_Key")
@NoArgsConstructor
@Accessors(fluent = true)
public final class MessageKeyEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  private Long id;


  @Column(name = "Key", nullable = false, unique = true)
  @Getter
  private String key;

  public MessageKeyEntity(@NotNull final String key) {
    this.key = key;
  }
}
