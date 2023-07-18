package dev.dotspace.network.library.spigot.scoreboard;

import dev.dotspace.network.library.provider.Provider;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Handle {@link ISidebar}. [GET, CREATE, REMOVE]
 */
public interface ISidebarProvider extends Provider {
  /**
   * Get the {@link ISidebar} for a {@link Player}.
   *
   * @param player to get present {@link ISidebar} from.
   * @return instance of {@link ISidebar} wrapped in {@link Optional}.
   * Empty if absent(no {@link ISidebar} set to {@link Player}).
   */
  @NotNull Optional<ISidebar> sidebar(@NotNull final Player player);

  /**
   * Get present {@link ISidebar} of Player or create a new one if absent.
   *
   * @param player to get or create {@link ISidebar} for.
   * @return currently active {@link ISidebar} for {@link Player}.
   */
  @NotNull ISidebar sidebarOrCreate(@NotNull final Player player);

  /**
   * Remove and delete {@link ISidebar} of {@link Player}.
   *
   * @param player to remove {@link ISidebar} from.
   * @return removed {@link ISidebar} from {@link Player}.
   */
  @NotNull Optional<ISidebar> removeSidebar(@NotNull final Player player);
}
