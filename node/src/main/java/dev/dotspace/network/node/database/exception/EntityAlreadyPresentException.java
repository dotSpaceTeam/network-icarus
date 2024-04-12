package dev.dotspace.network.node.database.exception;

import org.jetbrains.annotations.Nullable;


public class EntityAlreadyPresentException extends EntityException {
  public EntityAlreadyPresentException(@Nullable Object element,
                                       @Nullable String message) {
    super(element, message);
  }
}
