package dev.dotspace.network.library.game;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import dev.dotspace.network.library.game.message.ComponentBuilder;
import dev.dotspace.network.library.game.message.IComponentBuilder;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.units.qual.Acceleration;
import org.jetbrains.annotations.NotNull;


/**
 * Module
 */
@Singleton
@Log4j2
public final class GameModule extends AbstractModule {
  /**
   * Singleton get instance.
   */
  @Getter
  @Accessors(fluent=true)
  private final static @NotNull GameModule instance;

  static {
    instance = new GameModule();
    log.info("Create singleton GameModule.");
  }

  /**
   * Configure library module.
   */
  @Override
  protected void configure() {
    //Bind component builder.
    this.bind(IComponentBuilder.class).to(ComponentBuilder.class);
  }
}
