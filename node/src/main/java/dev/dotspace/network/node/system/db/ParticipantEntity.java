package dev.dotspace.network.node.system.db;

import dev.dotspace.network.library.system.IParticipant;
import dev.dotspace.network.library.system.ParticipantType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Entity
@Table(name="Participant",
    uniqueConstraints={@UniqueConstraint(columnNames={"Identifier", "Type"})})
@NoArgsConstructor
@Accessors(fluent=true)
public final class ParticipantEntity implements IParticipant {
  /**
   * Identity of element
   */
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  /**
   * See {@link IParticipant#identifier()}.
   */
  @Column(name="Identifier", nullable=false, unique=true)
  private String identifier;

  /**
   * See {@link IParticipant#type()}.
   */
  @Column(name="Type", nullable=false)
  private ParticipantType type;

  public ParticipantEntity(@NotNull final String identifier,
                           @NotNull final ParticipantType type) {
    this.identifier = identifier;
    this.type = type;
  }
}