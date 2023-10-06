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
public final class DatabaseEntityEvent<TYPE, SCHEMA> extends AbstractDatabaseEvent {
  private final @NotNull TYPE type;

  private final @NotNull Class<SCHEMA> schemaClass;

  /**
   * See {@link ApplicationEvent#ApplicationEvent(Object)}.
   */
  public DatabaseEntityEvent(@NotNull final Object source,
                             @NotNull final DatabaseManipulation manipulation,
                             @NotNull final TYPE type,
                             @NotNull final Class<SCHEMA> schemaClass) {
    super(source, manipulation);
    this.type = type;
    this.schemaClass = schemaClass;
  }
}
