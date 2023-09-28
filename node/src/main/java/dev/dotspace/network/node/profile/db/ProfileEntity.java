package dev.dotspace.network.node.profile.db;


import dev.dotspace.network.library.profile.IProfile;
import dev.dotspace.network.library.profile.ProfileType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Objects;


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
