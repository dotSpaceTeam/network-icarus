package dev.dotspace.network.library.game.scoreboard;

import dev.dotspace.network.library.provider.Provider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Handle {@link GameSidebar}. [GET, CREATE, REMOVE]
 */
public interface GameSidebarProvider<PLAYER, TEXT> extends Provider {
    /**
     * Get the {@link GameSidebar} for a {@link PLAYER}.
     *
     * @param player to get present {@link GameSidebar} from.
     * @return instance of {@link GameSidebar} wrapped in {@link Optional}.
     * Empty if absent(no {@link GameSidebar} set to {@link PLAYER}).
     * @throws NullPointerException if player is null.
     */
    @NotNull Optional<GameSidebar<TEXT>> sidebar(@Nullable final PLAYER player);

    /**
     * Get present {@link GameSidebar} of Player or create a new one if absent.
     *
     * @param player to get or create {@link GameSidebar} for.
     * @return currently active {@link GameSidebar} for {@link PLAYER}.
     * @throws NullPointerException if player is null.
     */
    @NotNull GameSidebar<TEXT> create(@Nullable final PLAYER player);

    /**
     * Remove and delete {@link GameSidebar} of {@link PLAYER}.
     *
     * @param player to remove {@link GameSidebar} from.
     * @return removed {@link GameSidebar} from {@link PLAYER}.
     * @throws NullPointerException if player is null.
     */
    @NotNull Optional<GameSidebar<TEXT>> remove(@Nullable final PLAYER player);
}
