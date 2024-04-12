package dev.dotspace.network.node.database.exception;

import org.jetbrains.annotations.Nullable;


public class IllegalEntityException extends EntityException {
  public IllegalEntityException(@Nullable final Object element,
                                @Nullable final String message) {
    super(element, message);
  }

  public IllegalEntityException(@Nullable final String message) {
    super(null, message);
  }
}
