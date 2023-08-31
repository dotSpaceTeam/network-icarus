package dev.dotspace.network.node.message_old.db;

import dev.dotspace.network.library.message.old.IMessage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

@Entity
@Table(name = "Message",
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {"Key", "Locale", "Message"}),
    @UniqueConstraint(columnNames = {"Key", "Locale"})
  })
@NoArgsConstructor
@Accessors(fluent = true)
public final class MessageEntity implements IMessage {
  /**
   * Identity of element
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  private Long id;
  @ManyToOne
  @JoinColumn(name = "Key", nullable = false)
  private MessageKeyEntity key;

  @Column(name = "Locale", nullable = false)
  private String locale;

  @Column(name = "Message", nullable = false)
  @Getter
  @Setter
  private String message;

  public MessageEntity(@NotNull final MessageKeyEntity key,
                       @NotNull final String locale,
                       @NotNull final String message) {
    this.key = key;
    this.locale = locale;
    this.message = message;
  }

  /**
   * See {@link IMessage#key()}.
   */
  @Override
  public @NotNull String key() {
    return this.key.key();
  }

  /**
   * See {@link IMessage#locale()}.
   */
  @Override
  public @NotNull Locale locale() {
    return Locale.forLanguageTag(this.locale);
  }
}
