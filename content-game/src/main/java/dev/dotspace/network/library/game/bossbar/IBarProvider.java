package dev.dotspace.network.library.game.bossbar;

import dev.dotspace.common.ObjectLabel;
import dev.dotspace.network.library.provider.Provider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IBarProvider<PLAYER, BAR> extends Provider {
  /**
   * List al boss bars of player.
   *
   * @param player to get all bars.
   * @return return immutable list of bars.
   */
  @NotNull List<Map.Entry<String, BAR>> list(@Nullable final PLAYER player);

  /**
   * Get a specific bar.
   *
   * @param player to get bar from.
   * @param name   of bar to get.
   * @return bar in optional.
   */
  @NotNull Optional<BAR> bar(@Nullable final PLAYER player,
                             @Nullable final String name);

  /**
   * Get a specific bar.
   *
   * @param player to get bar from.
   * @param bar    to set as bar.
   * @param name   to identify bar.
   * @return bar in optional.
   */
  @NotNull Optional<BAR> set(@Nullable final PLAYER player,
                             @Nullable final BAR bar,
                             @Nullable final String name);

  /**
   * Remove a bar from player.
   *
   * @param player to remove bar from.
   * @param name   of bar to remove.
   * @return removed bar if present.
   */
  @NotNull Optional<BAR> remove(@Nullable final PLAYER player,
                                @Nullable final String name);
}
