package dev.dotspace.network.node.profile.db.session;

import dev.dotspace.network.library.connection.IAddress;
import dev.dotspace.network.library.connection.ImmutableAddress;
import dev.dotspace.network.library.profile.session.ISession;
import dev.dotspace.network.node.profile.db.ProfileEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

@Entity
@Table(name = "Session")
@NoArgsConstructor
@Accessors(fluent = true)
public final class SessionEntity implements ISession {
  /**
   * Identity of element
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  private Long id;

  @ManyToOne
  @JoinColumn(name = "Profile", nullable = false)
  @Getter
  private ProfileEntity profile;

  /**
   * Date session was opened.
   */
  @Column(nullable = false)
  @Getter
  private Date startDate;
  /**
   * Date session was closed.
   */
  @Column
  @Getter
  @Setter
  private Date endDate;

  @Column(name = "Address")
  private String address;

  public SessionEntity(@NotNull final ProfileEntity profile,
                       @NotNull final Date startDate,
                       @Nullable final Date endDate,
                       @NotNull final String address) {
    this.profile = profile;
    this.startDate = startDate;
    this.endDate = endDate;
    this.address = address;
  }

  @Override
  public @NotNull Long sessionId() {
    return this.id;
  }

  @Override
  public long duration() {
    //End date of session.
    if (this.endDate == null) {
      return 0;
    }
    //Calculate difference
    return this.endDate.getTime() - this.startDate.getTime();
  }

  /**
   * See {@link ISession#connectionAddress()}
   */
  @Override
  public @NotNull IAddress connectionAddress() {
    return ImmutableAddress.of(this.address);
  }

  /**
   * State of session.
   *
   * @return true if endDate is not null.
   */
  public boolean closed() {
    return this.endDate != null;
  }
}
