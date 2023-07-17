package dev.dotspace.network.node.runtime.db;

import dev.dotspace.network.library.runtime.IRuntime;
import dev.dotspace.network.library.runtime.RuntimeType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "Runtime",
  uniqueConstraints = {@UniqueConstraint(columnNames = {"RuntimeId", "Type"})})
@NoArgsConstructor
@Accessors(fluent = true)
public final class RuntimeEntity implements IRuntime {
  /**
   * Identity of element
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  private Long id;

  /**
   * See {@link IRuntime#runtimeId()}.
   */
  @Column(name = "RuntimeId", nullable = false, unique = true)
  @Getter
  private String runtimeId;

  /**
   * See {@link IRuntime#type()}.
   */
  @Column(name = "Type", nullable = false)
  @Getter
  private RuntimeType type;

  public RuntimeEntity(@NotNull final String runtimeId,
                       @NotNull final RuntimeType type) {
    this.runtimeId = runtimeId;
    this.type = type;
  }
}