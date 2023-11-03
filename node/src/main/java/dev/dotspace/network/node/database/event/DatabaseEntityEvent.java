package dev.dotspace.network.node.database.event;

import dev.dotspace.network.library.data.DataManipulation;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;


/**
 * Post database manipulation.
 */
@Getter
@Accessors(fluent=true)
public final class DatabaseEntityEvent<TYPE, SCHEMA> extends AbstractDatabaseEvent {
  /**
   * Entity that was manipulated.
   */
  private final @NotNull TYPE type;
  /**
   * Schema to create java class.
   */
  private final @NotNull Class<SCHEMA> schemaClass;

  /**
   * See {@link ApplicationEvent#ApplicationEvent(Object)}.
   */
  public DatabaseEntityEvent(@NotNull final Object source,
                             @NotNull final DataManipulation manipulation,
                             @NotNull final TYPE type,
                             @NotNull final Class<SCHEMA> schemaClass) {
    super(source, manipulation);
    this.type = type;
    this.schemaClass = schemaClass;
  }
}
