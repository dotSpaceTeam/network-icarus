package dev.dotspace.network.node.message.db;

import dev.dotspace.network.library.message.content.IPersistentMessage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;


@Entity
@Table(name="PersistentMessage",
    uniqueConstraints={
        @UniqueConstraint(columnNames={"Key", "Locale", "Message"}),
        @UniqueConstraint(columnNames={"Key", "Locale"})
    })
@NoArgsConstructor
@Accessors(fluent=true)
public final class PersistentMessageEntity implements IPersistentMessage {
  /**
   * Identity of element
   */
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Getter
  private Long id;
  /**
   * Key for message.
   */
  @ManyToOne
  @JoinColumn(name="Key", nullable=false)
  private PersistentMessageKeyEntity key;
  /**
   * Locale of message.
   */
  @Column(name="Locale", nullable=false)
  private String locale;
  /**
   * Content.
   */
  @Column(name="Message", nullable=false, length=4096)
  @Getter
  @Setter
  private String message;

  public PersistentMessageEntity(@NotNull final PersistentMessageKeyEntity key,
                                 @NotNull final String locale,
                                 @NotNull final String message) {
    this.key = key;
    this.locale = locale;
    this.message = message;
  }

  /**
   * See {@link IPersistentMessage#key()}.
   */
  @Override
  public @NotNull String key() {
    return this.key.key();
  }

  /**
   * See {@link IPersistentMessage#locale()}.
   */
  @Override
  public @NotNull Locale locale() {
    return Locale.forLanguageTag(this.locale);
  }
}
