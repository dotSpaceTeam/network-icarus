package dev.dotspace.network.library.game.scoreboard;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * This object represents the {@link ISidebar} shown to a player.
 */
public interface ISidebar<TEXT> {
    /**
     * Returns the current title of {@link ISidebar}.
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
    @NotNull ISidebar<TEXT> title(@Nullable final TEXT text);

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
    @NotNull ISidebar<TEXT> line(final int line,
                                 @Nullable final TEXT text);

    /**
     * Update all lines of sidebar.
     *
     * @param components list the update with.
     * @return class instance.
     */
    @NotNull ISidebar<TEXT> lines(@Nullable final Collection<TEXT> components);

    /**
     * Similar to {@link ISidebar#lines(Collection)}.
     */

    default @NotNull ISidebar<TEXT> lines(@Nullable final TEXT... components) {
        return this.lines(Arrays.asList(components));
    }

    /**
     * Remove a line of {@link ISidebar}.
     *
     * @param line to delete of sidebar.
     * @return class instance.
     */
    @NotNull ISidebar<TEXT> deleteLine(final int line);

    /**
     * Remove all lines from {@link ISidebar}.
     *
     * @return class instance.
     */
    @NotNull ISidebar<TEXT> clear();

    boolean delete();

    boolean deleted();
}
