package dev.dotspace.network.library.prohibit;

import jakarta.validation.constraints.NotNull;


public interface IProhibitReason {
  /**
   * Type for prohibit reason.
   *
   * @return defines the reason affiliation.
   */
  @NotNull ProhibitType type();

  /**
   * How the reason should be named for commands.
   *
   * @return name of reason.
   */
  @NotNull String name();

  /**
   * How the reason should be titled.
   *
   * @return title of reason.
   */
  @NotNull String title();

  /**
   * Description of prohibit.
   *
   * @return description as name.
   */
  @NotNull String description();
}
