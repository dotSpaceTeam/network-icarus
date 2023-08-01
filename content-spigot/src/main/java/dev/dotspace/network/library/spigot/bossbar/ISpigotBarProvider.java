package dev.dotspace.network.library.spigot.bossbar;

import dev.dotspace.network.library.game.bossbar.IBarProvider;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Extension of {@link IBarProvider}.
 */
public interface ISpigotBarProvider extends IBarProvider<Player, BossBar> {
  /**
   * Create new boss bar for player.
   *
   * @param player    to create boss bar for.
   * @param name      to identify bar.
   * @param component text of bar.
   * @param progress  state of bar.
   * @param color     for bar to set to player.
   * @param overlay   type of bar.
   * @return created bar.
   */
  @NotNull BossBar create(@Nullable final Player player,
                          @Nullable final String name,
                          @Nullable final Component component,
                          final float progress,
                          @Nullable final BossBar.Color color,
                          @Nullable final BossBar.Overlay overlay);
}
