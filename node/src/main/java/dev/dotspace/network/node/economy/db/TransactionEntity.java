package dev.dotspace.network.node.economy.db;

import dev.dotspace.network.library.economy.ITransaction;
import dev.dotspace.network.library.economy.TransactionType;
import dev.dotspace.network.library.profile.IProfile;
import dev.dotspace.network.node.profile.db.ProfileEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Entity
@Table(name = "Transaction")
@NoArgsConstructor
@Accessors(fluent = true)
final class TransactionEntity implements ITransaction {
  /**
   * Identity of element
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  private Long id;

  @ManyToOne
  @JoinColumn(name = "Profile", nullable = false)
  private ProfileEntity profile;

  @ManyToOne
  @JoinColumn(name = "Currency", nullable = false)
  private CurrencyEntity currency;

  /**
   * See {@link IProfile#uniqueId()}.
   */
  @Column(nullable = false)
  @Getter
  private int amount;

  /**
   * See {@link IProfile#profileType()}.
   */
  @Column(name = "Type", nullable = false)
  @Getter
  private TransactionType transactionType;

  /**
   * Time transaction was inserted.
   */
  @Column(name = "Created", nullable = false)
  private Date created;

  /**
   * Call on create (sql).
   */
  @PrePersist
  private void onCreate() {
    this.created = new Date();
  }

  public TransactionEntity(@NotNull final ProfileEntity profile,
                           @NotNull final CurrencyEntity currency,
                           final int amount,
                           @NotNull final TransactionType transactionType) {
    this.profile = profile;
    this.currency = currency;
    this.amount = amount;
    this.transactionType = transactionType;
  }
}
