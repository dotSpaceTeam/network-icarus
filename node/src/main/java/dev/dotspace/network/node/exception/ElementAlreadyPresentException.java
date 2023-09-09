package dev.dotspace.network.node.exception;

import org.jetbrains.annotations.Nullable;


public class ElementAlreadyPresentException extends ElementException {
  public ElementAlreadyPresentException(@Nullable Object element,
                                        @Nullable String message) {
    super(element, message);
  }
}
