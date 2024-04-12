package dev.dotspace.network.node.economy.db;

import dev.dotspace.network.library.economy.ICurrency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Getter
@Entity
@Table(name="Currency",
    uniqueConstraints={@UniqueConstraint(columnNames={"Name", "Symbol", "Display", "DisplayPlural"})})
@NoArgsConstructor
@Accessors(fluent=true)
public final class CurrencyEntity implements ICurrency {
  /**
   * Identity of element
   */
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  /**
   * See {@link ICurrency#symbol()}.
   */
  @Column(name="Name", nullable=false, unique=true)
  private String name;

  /**
   * See {@link ICurrency#symbol()}.
   */
  @Column(name="Symbol", nullable=false)
  @Setter
  private String symbol;

  /**
   * See {@link ICurrency#display()}.
   */
  @Column(name="Display", nullable=false)
  @Setter
  private String display;

  /**
   * See {@link ICurrency#displayPlural()}.
   */
  @Column(name="DisplayPlural")
  @Setter
  private String displayPlural;

  public CurrencyEntity(@NotNull final String name,
                        @NotNull final String symbol,
                        @NotNull final String display,
                        @Nullable final String displayPlural) {
    this.symbol = symbol;
    this.name = name;
    this.display = display;
    this.displayPlural = displayPlural;
  }
}
