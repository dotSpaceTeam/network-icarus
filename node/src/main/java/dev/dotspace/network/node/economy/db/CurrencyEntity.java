package dev.dotspace.network.node.economy.db;
;
import dev.dotspace.network.library.economy.ICurrency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(name = "Currency",
  uniqueConstraints = {@UniqueConstraint(columnNames = {"Symbol", "Name", "Plural"})})
@NoArgsConstructor
@Accessors(fluent = true)
public final class CurrencyEntity implements ICurrency {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  private Long id;

  /**
   * See {@link ICurrency#symbol()}.
   */
  @Column(name = "Symbol", nullable = false, unique = true)
  @Getter
  private String symbol;

  /**
   * See {@link ICurrency#name()}.
   */
  @Column(name = "Name", nullable = false)
  @Getter
  private String name;

  /**
   * See {@link ICurrency#pluralName()}.
   */
  @Column(name = "Plural", nullable = false)
  @Getter
  private String pluralName;

  public CurrencyEntity(@NotNull final String symbol,
                        @NotNull String name,
                        @Nullable String pluralName) {
    this.symbol = symbol;
    this.name = name;
    this.pluralName = pluralName;
  }
}
