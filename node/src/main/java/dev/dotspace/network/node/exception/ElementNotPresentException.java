package dev.dotspace.network.node.exception;

import org.jetbrains.annotations.Nullable;


public class ElementNotPresentException extends ElementException {
  public ElementNotPresentException(@Nullable Object element,
                                    @Nullable String message) {
    super(element, message);
  }

  public ElementNotPresentException(@Nullable String message) {
    super(null, message);
  }
}
