package dev.dotspace.network.node.runtime.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

@Entity
@Table(name = "NodeRestRequest")
@NoArgsConstructor
@Accessors(fluent = true)
public final class RestRequestEntity implements IRestRequest {
  /**
   * Identity of element
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  private Long id;

  /**
   * See {@link IRestRequest#url()}.
   */
  @Column(name = "Url", nullable = false)
  @Getter
  private String url;

  /**
   * See {@link IRestRequest#url()}.
   */
  @Column(name = "Client", nullable = false)
  @Getter
  private String client;

  /**
   * See {@link IRestRequest#method()}.
   */
  @Column(name = "Method", nullable = false)
  @Getter
  private String method;

  /**
   * See {@link IRestRequest#processTime()}.
   */
  @Column(name = "Time", nullable = false)
  @Getter
  private long processTime;

  /**
   * See {@link IRestRequest#success()}.
   */
  @Column(name = "Success", nullable = false)
  @Getter
  private boolean success;

  /**
   * See {@link IRestRequest#note()}.
   */
  @Column(name = "Note")
  @Getter
  private String note;

  /**
   * See {@link IRestRequest#timestamp()}.
   */
  @Column(name = "Timestamp", nullable = false)
  @Getter
  private Date timestamp;

  public RestRequestEntity(@NotNull final String url,
                           @NotNull final String client,
                           @NotNull final String method,
                           final long processTime,
                           final boolean success,
                           @Nullable final String note,
                           @NotNull final Date timestamp) {
    this.url = url;
    this.client = client;
    this.method = method;
    this.processTime = processTime;
    this.success = success;
    this.note = note;
    this.timestamp = timestamp;
  }
}
