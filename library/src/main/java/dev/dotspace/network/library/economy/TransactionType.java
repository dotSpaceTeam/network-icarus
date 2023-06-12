package dev.dotspace.network.library.economy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor()
@Accessors(fluent = true)
public enum TransactionType {

  /**
   * If java profile
   */
  WITHDRAW(0),
  /**
   * If profile is a bedrock profile.
   */
  DEPOSIT(1);

  /**
   * If of type.
   */
  private final int id;

  //static

  /**
   * Get {@link TransactionType} from id. ({@link TransactionType#id})
   *
   * @param id to get type from.
   * @return present {@link TransactionType} owner of the id.
   * @throws IndexOutOfBoundsException if id is not a valid type.
   */
  public static @NotNull TransactionType fromId(final int id) throws IndexOutOfBoundsException {
    for (final TransactionType value : TransactionType.values()) { //Loop trough every type.
      if (value.id != id) { //Goto next enum value if no match.
        continue;
      }
      return value;
    }
    throw new IndexOutOfBoundsException("Given id has no matching type.");
  }
}
