package dev.dotspace.network.node.database.event;

import dev.dotspace.network.node.database.manipulate.DatabaseManipulation;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;


/**
 * Base of db events.
 */
@Getter
@Accessors(fluent=true)
public abstract class AbstractDatabaseEvent extends ApplicationEvent {
  private final @NotNull DatabaseManipulation manipulation;

  /**
   * See {@link ApplicationEvent#ApplicationEvent(Object)}.
   */
  public AbstractDatabaseEvent(@NotNull final Object source,
                               @NotNull final DatabaseManipulation manipulation) {
    super(source);
    this.manipulation = manipulation;
  }
}
