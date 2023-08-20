package dev.dotspace.network.library.game.event;

import org.jetbrains.annotations.NotNull;


/**
 * Listener for game events.
 */
public interface GameListener<PLUGIN> {
  /**
   * Get plugin stored in variable.
   * Plugin this listener is listed for.
   */
  @NotNull PLUGIN plugin();
}
