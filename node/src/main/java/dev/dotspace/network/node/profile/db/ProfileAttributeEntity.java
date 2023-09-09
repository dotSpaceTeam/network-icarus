package dev.dotspace.network.node.profile.db;

import dev.dotspace.network.library.profile.IProfileAttribute;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Entity
@Table(name="ProfileAttributes",
    uniqueConstraints={@UniqueConstraint(columnNames={"Profile", "Key"})})
@NoArgsConstructor
@Accessors(fluent=true)
public final class ProfileAttributeEntity implements IProfileAttribute {
  /**
   * Identity of element
   */
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Getter
  private Long id;

  @ManyToOne
  @JoinColumn(name="Profile", nullable=false)
  private ProfileEntity profile;

  @Column(nullable=false)
  @Getter
  private String key;

  @Column
  @Getter
  @Setter
  private String value;


  public ProfileAttributeEntity(@NotNull final ProfileEntity profile,
                                @NotNull final String key,
                                @Nullable final String value) {
    this.profile = profile;
    this.key = key;
    this.value = value;
  }
}
