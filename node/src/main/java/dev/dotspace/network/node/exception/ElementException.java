package dev.dotspace.network.node.exception;

import dev.dotspace.network.library.exception.LibraryException;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;


@Accessors(fluent=true)
public class ElementException extends LibraryException {
  @Getter
  private final @Nullable Object element;

  public ElementException(@Nullable final Object element,
                          @Nullable final String message) {
    super(message);
    this.element = element;
  }
}
