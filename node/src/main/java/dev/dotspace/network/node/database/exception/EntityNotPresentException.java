package dev.dotspace.network.node.database.exception;

import org.jetbrains.annotations.Nullable;


public class EntityNotPresentException extends EntityException {
  public EntityNotPresentException(@Nullable Object element,
                                   @Nullable String message) {
    super(element, message);
  }

  public EntityNotPresentException(@Nullable String message) {
    super(null, message);
  }
}
