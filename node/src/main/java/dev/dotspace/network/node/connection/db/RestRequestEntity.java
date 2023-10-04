package dev.dotspace.network.node.connection.db;

import dev.dotspace.network.library.connection.IRestRequest;
import dev.dotspace.network.library.system.IParticipant;
import dev.dotspace.network.library.system.ImmutableParticipant;
import dev.dotspace.network.node.system.db.ParticipantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Optional;


@Entity
@Table(name="RestRequest")
@NoArgsConstructor
@Accessors(fluent=true)
public final class RestRequestEntity implements IRestRequest {
  /**
   * Identity of element
   */
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  @Getter
  private Long id;

  /**
   * See {@link IRestRequest#path()}.
   */
  @Column(name="path", nullable=false)
  @Getter
  private String path;

  /**
   * See {@link IRestRequest#client()}.
   */
  @ManyToOne
  @JoinColumn(name="Client")
  private ParticipantEntity client;

  /**
   * See {@link IRestRequest#method()}.
   */
  @Column(name="Method", nullable=false)
  @Getter
  private String method;

  /**
   * See {@link IRestRequest#address()}.
   */
  @Column(name="Address", nullable=false)
  @Getter
  private String address;

  /**
   * See {@link IRestRequest#note()}.
   */
  @Column(name="Note")
  @Getter
  private String note;

  /**
   * See {@link IRestRequest#timestamp()}.
   */
  @Column(name="Timestamp", nullable=false)
  @Getter
  private Date timestamp;

  /**
   * See {@link IRestRequest#processTime()}.
   */
  @Column(name="Time", nullable=false)
  @Getter
  private long processTime;

  /**
   * See {@link IRestRequest#status()}.
   */
  @Column(name="Status", nullable=false)
  @Getter
  private int status;

  public RestRequestEntity(@NotNull final String path,
                           @Nullable final ParticipantEntity client,
                           @NotNull final String method,
                           @NotNull final String address,
                           @NotNull final String note,
                           @NotNull final Date timestamp,
                           final long processTime,
                           final int status) {
    this.path = path;
    this.client = client;
    this.method = method;
    this.address = address;
    this.note = note;
    this.timestamp = timestamp;
    this.processTime = processTime;
    this.status = status;
  }

  @Override
  public @NotNull IParticipant client() {
    return Optional.ofNullable(this.client)
        //Get if present -> map plain
        .map(ImmutableParticipant::of)
        //Else create empty client.
        .orElse(ImmutableParticipant.empty());
  }
}
