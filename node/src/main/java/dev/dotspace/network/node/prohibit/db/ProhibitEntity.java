package dev.dotspace.network.node.prohibit.db;

import dev.dotspace.network.library.profile.IProfile;
import dev.dotspace.network.library.profile.ImmutableProfile;
import dev.dotspace.network.library.prohibit.IProhibit;
import dev.dotspace.network.library.prohibit.IProhibitReason;
import dev.dotspace.network.library.prohibit.ImmutableProhibitReason;
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
@Table(name="Prohibit",
    uniqueConstraints={@UniqueConstraint(columnNames={"Reason", "Punished", "Executor", "Message", "Active"})})
@NoArgsConstructor
@Accessors(fluent=true)
public final class ProhibitEntity implements IProhibit {
  /**
   * Identity of element
   */
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name="Reason", nullable=false)
  private ProhibitReasonEntity reason;

  @ManyToOne
  @JoinColumn(name="Punished", nullable=false)
  private ProfileEntity punishedProfile;

  @ManyToOne
  @JoinColumn(name="Executor", nullable=false)
  private ProfileEntity executorProfile;

  /**
   * See {@link IProhibit#message()}
   */
  @Column(name="Message", nullable=false)
  @Setter
  @Getter
  private String message;

  /**
   * See {@link IProhibit#active()}
   */
  @Column(name="Active", nullable=false)
  @Setter
  @Getter
  private boolean active;

  /**
   * See {@link IProhibit#reason()}
   */
  @Override
  public IProhibitReason reason() {
    return ImmutableProhibitReason.of(this.reason);
  }

  /**
   * See {@link IProhibit#punishedProfile()}
   */
  @Override
  public IProfile punishedProfile() {
    return ImmutableProfile.of(this.punishedProfile);
  }

  /**
   * See {@link IProhibit#executorProfile()}
   */
  @Override
  public IProfile executorProfile() {
    return ImmutableProfile.of(this.executorProfile);
  }

  /**
   * Construct
   */
  public ProhibitEntity(@NotNull final ProhibitReasonEntity reason,
                        @NotNull final ProfileEntity punishedProfile,
                        @NotNull final ProfileEntity executorProfile,
                        @NotNull final String message,
                        final boolean active) {
    this.reason = reason;
    this.punishedProfile = punishedProfile;
    this.executorProfile = executorProfile;
    this.message = message;
    this.active = active;
  }
}
