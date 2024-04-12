package dev.dotspace.network.node.database;

import dev.dotspace.network.node.database.event.DatabaseEntityEvent;
import dev.dotspace.network.library.data.DataManipulation;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;


@Getter
@Log4j2
@Accessors(fluent=true)
public abstract class AbstractDatabase {
  /**
   * Spring event driver.
   */
  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  /**
   * Initialize database.
   */
  @PostConstruct
  public void init() {
    log.info("Initialized database name={}.", this.getClass().getSimpleName());
  }

  /**
   * Run event.
   */
  protected <TYPE, SCHEMA> void publishEvent(@NotNull final TYPE type,
                                             @NotNull final Class<SCHEMA> schemaClass,
                                             @NotNull final DataManipulation manipulation) {
    //Publish event.
    this.applicationEventPublisher.publishEvent(new DatabaseEntityEvent<>(this, manipulation, type, schemaClass));
  }
}
