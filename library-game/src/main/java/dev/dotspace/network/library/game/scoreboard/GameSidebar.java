package dev.dotspace.network.library.game.scoreboard;

import dev.dotspace.network.library.game.message.context.MessageContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * This object represents the {@link GameSidebar} shown to a player.
 */
public interface GameSidebar<TEXT> {
  /**
   * Returns the current title of {@link GameSidebar}.
   *
   * @return current title of sidebar.
   */
  @NotNull Optional<TEXT> title();

  /**
   * Update the current title of the sidebar.
   *
   * @param text to update title.
   * @return class instance.
   */
  @NotNull GameSidebar<TEXT> title(@Nullable final TEXT text);

  /**
   * Set message context to title.
   * See {@link GameSidebar#title(Object)}
   */
  @NotNull GameSidebar<TEXT> title(@Nullable final MessageContext messageContext);

  /**
   * Returns list of sidebar text.
   *
   * @return list of TEXT.
   */
  @NotNull List<TEXT> lines();

  /**
   * Get the text of the line number.
   *
   * @param line to get text from.
   * @return content of line wrapped {@link Optional}.
   */
  @NotNull Optional<TEXT> line(final int line);

  /**
   * Update line of sidebar.
   *
   * @param line to update.
   * @param text to set on the line.
   * @return class instance.
   */
  @NotNull GameSidebar<TEXT> line(final int line,
                                  @Nullable final TEXT text);

  /**
   * Set message context to line.
   * See  {@link GameSidebar#line(int, Object)}.
   */
  @NotNull GameSidebar<TEXT> line(final int line,
                                  @Nullable final MessageContext messageContext);

  /**
   * Update all lines of sidebar.
   *
   * @param components list the update with.
   * @return class instance.
   */
  @NotNull GameSidebar<TEXT> lines(@Nullable final Collection<TEXT> components);

  /**
   * Update all lines of sidebar.
   * See {@link GameSidebar#lines(Collection)}
   */
  @NotNull GameSidebar<TEXT> lines(@Nullable final MessageContext messageContext);

  /**
   * Similar to {@link GameSidebar#lines(Collection)}.
   */

  default @NotNull GameSidebar<TEXT> lines(@Nullable final TEXT... components) {
    return this.lines(Arrays.asList(components));
  }

  /**
   * Remove a line of {@link GameSidebar}.
   *
   * @param line to delete of sidebar.
   * @return class instance.
   */
  @NotNull GameSidebar<TEXT> deleteLine(final int line);

  /**
   * Remove all lines from {@link GameSidebar}.
   *
   * @return class instance.
   */
  @NotNull GameSidebar<TEXT> clear();

  boolean delete();

  boolean deleted();
}
