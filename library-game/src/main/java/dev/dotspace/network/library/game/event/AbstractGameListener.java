package dev.dotspace.network.library.game.event;

import com.google.inject.Inject;
import lombok.Getter;
import lombok.experimental.Accessors;


/**
 * Implementation for {@link GameListener}
 *
 * @param <PLUGIN> generic type of plugin.
 */
@Accessors(fluent = true)
public abstract class AbstractGameListener<PLUGIN> implements GameListener<PLUGIN> {
  /**
   * Get trough {@link GameListener#plugin()}
   */
  @Inject
  @Getter
  private PLUGIN plugin;
}
