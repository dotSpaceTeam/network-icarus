package dev.dotspace.network.node.listener;

import dev.dotspace.network.node.database.event.DatabaseEntityEvent;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@Log4j2
public final class DatabaseListener {
  /**
   * Handle db
   */
  @EventListener
  public void handleDatabase(@NotNull final DatabaseEntityEvent<?, ?> event) {
    System.out.println("Type "+event.manipulation());
    System.out.println("Obj "+event.type().getClass().getName());
    System.out.println("Schem "+event.schemaClass().getName());
  }
}
