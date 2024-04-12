package dev.dotspace.network.node.profile.db;


import dev.dotspace.network.library.profile.IProfile;
import dev.dotspace.network.library.profile.ProfileType;
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
@Table(name="Profile",
    uniqueConstraints={@UniqueConstraint(columnNames={"Uid", "Platform"}),
        @UniqueConstraint(columnNames={"Uid", "Name", "Platform"})
    })
@NoArgsConstructor
@Getter
@Accessors(fluent=true)
public final class ProfileEntity implements IProfile {
  /**
   * Identity of element
   */
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  /**
   * See {@link IProfile#uniqueId()}.
   */
  @Column(name="Uid", nullable=false, unique=true)
  private String uniqueId;

  /**
   * See {@link IProfile#uniqueId()}.
   */
  @Column(name="Name", nullable=false, unique=true)
  @Setter
  private String name;

  /**
   * See {@link IProfile#profileType()}.
   */
  @Column(name="Platform", nullable=false)
  private ProfileType profileType;

  public ProfileEntity(@NotNull final String uniqueId,
                       @NotNull final String name,
                       @NotNull final ProfileType profileType) {
    this.uniqueId = uniqueId;
    this.name = name;
    this.profileType = profileType;
  }
}
