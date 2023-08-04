package dev.dotspace.network.library.game.scoreboard;

import dev.dotspace.network.library.provider.Provider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Handle {@link ISidebar}. [GET, CREATE, REMOVE]
 */
public interface ISidebarProvider<PLAYER, TEXT> extends Provider {
    /**
     * Get the {@link ISidebar} for a {@link PLAYER}.
     *
     * @param player to get present {@link ISidebar} from.
     * @return instance of {@link ISidebar} wrapped in {@link Optional}.
     * Empty if absent(no {@link ISidebar} set to {@link PLAYER}).
     * @throws NullPointerException if player is null.
     */
    @NotNull Optional<ISidebar<TEXT>> sidebar(@Nullable final PLAYER player);

    /**
     * Get present {@link ISidebar} of Player or create a new one if absent.
     *
     * @param player to get or create {@link ISidebar} for.
     * @return currently active {@link ISidebar} for {@link PLAYER}.
     * @throws NullPointerException if player is null.
     */
    @NotNull ISidebar<TEXT> create(@Nullable final PLAYER player);

    /**
     * Remove and delete {@link ISidebar} of {@link PLAYER}.
     *
     * @param player to remove {@link ISidebar} from.
     * @return removed {@link ISidebar} from {@link PLAYER}.
     * @throws NullPointerException if player is null.
     */
    @NotNull Optional<ISidebar<TEXT>> remove(@Nullable final PLAYER player);
}
