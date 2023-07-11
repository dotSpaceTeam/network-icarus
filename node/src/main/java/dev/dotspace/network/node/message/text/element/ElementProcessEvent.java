package dev.dotspace.network.node.message.text.element;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;

@Accessors(fluent = true)
public final class ElementProcessEvent extends ApplicationEvent {
  @Getter
  private final @NotNull ElementType type;

  public ElementProcessEvent(@NotNull final Object source,
                             @NotNull final ElementType type) {
    super(source);
    this.type = type;
  }
}
