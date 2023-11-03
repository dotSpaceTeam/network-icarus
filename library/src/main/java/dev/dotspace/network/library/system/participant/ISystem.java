package dev.dotspace.network.library.system.participant;

import org.jetbrains.annotations.NotNull;


/**
 * Part of icarus -> client or node.
 */
public interface ISystem {
  /**
   * Parameter of system.
   */
  @NotNull IParticipant participant();
}
