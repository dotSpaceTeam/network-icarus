package dev.dotspace.network.node.profile.db.experience;

import dev.dotspace.network.library.profile.experience.IExperience;
import dev.dotspace.network.node.profile.db.ProfileEntity;
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


@Entity
@Table(name="ProfileExperience",
    uniqueConstraints={@UniqueConstraint(columnNames={"Profile", "Name"})})
@NoArgsConstructor
@Accessors(fluent=true)
public final class ExperienceEntity implements IExperience {
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

  @Column(name = "Name", nullable=false)
  @Getter
  private String name;


  @Column(nullable=false)
  @Setter
  @Getter
  private long experience;


  public ExperienceEntity(@NotNull final ProfileEntity profile,
                          @NotNull final String name,
                          final long experience) {
    this.profile = profile;
    this.name = name;
    this.experience = experience;
  }

  @Override
  public long level() {
    return LevelFunction.function().levelFromExperience(this.experience);
  }
}
