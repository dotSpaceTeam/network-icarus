package dev.dotspace.network.library.spigot.scoreboard;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * This object represents the {@link ISidebar} shown to a player.
 */
public interface ISidebar {
  /**
   * Returns the current title of {@link ISidebar}.
   *
   * @return current title of sidebar.
   */
  @NotNull Optional<Component> title();

  /**
   * Update the current title of the sidebar.
   *
   * @param text to update title.
   * @return class instance.
   */
  @NotNull ISidebar title(@NotNull final Component text);

  /**
   * Returns list of sidebar text.
   *
   * @return list of TEXT.
   */
  @NotNull List<Component> lines();

  /**
   * Get the text of the line number.
   *
   * @param line to get text from.
   * @return content of line as {@link Component} wrapped in {@link Optional}.
   */
  @NotNull Optional<Component> line(final int line);

  /**
   * Update line of sidebar.
   *
   * @param line to update.
   * @param text to set on the line.
   * @return class instance.
   */
  @NotNull ISidebar line(final int line,
                         @NotNull final Component text);

  /**
   * Update all lines of sidebar.
   *
   * @param components list the update with.
   * @return class instance.
   */
  @NotNull ISidebar lines(@NotNull final Collection<Component> components);

  /**
   * Similar to {@link ISidebar#lines(Collection)}.
   */
  default @NotNull ISidebar lines(@NotNull final Component... components) {
    return this.lines(Arrays.asList(components));
  }

  /**
   * Remove a line of {@link ISidebar}.
   *
   * @param line to delete of sidebar.
   * @return class instance.
   */
  @NotNull ISidebar deleteLine(final int line);

  /**
   * Remove all lines from {@link ISidebar}.
   *
   * @return class instance.
   */
  @NotNull ISidebar clear();
}
