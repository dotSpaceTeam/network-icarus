package dev.dotspace.network.node.prohibit.db;

import dev.dotspace.network.library.prohibit.IProhibitReason;
import dev.dotspace.network.library.prohibit.ProhibitType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;


@Entity
@Table(name="ProhibitReason",
    uniqueConstraints={@UniqueConstraint(columnNames={"Type", "Name"})})
@NoArgsConstructor
@Getter
@Accessors(fluent=true)
public final class ProhibitReasonEntity implements IProhibitReason {
  /**
   * Identity of element
   */
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  /**
   * See {@link IProhibitReason#type()}.
   */
  @Column(name="Type", nullable=false)
  private ProhibitType type;

  /**
   * See {@link IProhibitReason#name()}.
   */
  @Column(name="Name", nullable=false)
  private String name;

  /**
   * See {@link IProhibitReason#title()}.
   */
  @Column(name="Title", nullable=false)
  @Setter
  private String title;

  /**
   * See {@link IProhibitReason#description()}.
   */
  @Column(name="Description", nullable=false)
  @Setter
  private String description;

  /**
   * Construct
   */
  public ProhibitReasonEntity(@NotNull final ProhibitType type,
                              @NotNull final String name,
                              @NotNull final String title,
                              @NotNull final String description) {
    this.type = type;
    this.name = name;
    this.title = title;
    this.description = description;
  }
}
