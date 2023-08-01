package dev.dotspace.network.library.spigot.bossbar;

import dev.dotspace.common.ObjectLabel;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class BarProvider implements ISpigotBarProvider {
  /**
   * Store map of bars.
   */
  private final Map<Player, List<Map.Entry<String, BossBar>>> barMap;

  public BarProvider() {
    this.barMap = new HashMap<>();
  }

  /**
   * See {@link ISpigotBarProvider#list(Object)}
   */
  @Override
  public @NotNull List<Map.Entry<String, BossBar>> list(@Nullable final Player player) {
    //Null check
    Objects.requireNonNull(player);

    return Optional
      //Get list of entries.
      .ofNullable(this.barMap.get(player))
      //Empty map.
      .orElseGet(Collections::emptyList);
  }

  /**
   * See {@link ISpigotBarProvider#bar(Object, String)}
   */
  @Override
  public @NotNull Optional<BossBar> bar(@Nullable final Player player,
                                        @Nullable final String name) {
    //Null check
    Objects.requireNonNull(player);
    Objects.requireNonNull(name);

    return Optional.empty();
  }

  /**
   * See {@link ISpigotBarProvider#set(Object, Object, String)}
   */
  @Override
  public @NotNull Optional<BossBar> set(@Nullable final Player player,
                                        @Nullable final BossBar bossBar,
                                        @Nullable final String name) {
    return Optional.empty();
  }

  /**
   * See {@link ISpigotBarProvider#remove(Object, String)}
   */
  @Override
  public @NotNull Optional<BossBar> remove(@Nullable final Player player,
                                           @Nullable final String name) {
    return Optional.empty();
  }

  /**
   * See {@link ISpigotBarProvider#create(Player, String, Component, float, BossBar.Color, BossBar.Overlay)}
   */
  @Override
  public @NotNull BossBar create(@Nullable final Player player,
                                 @Nullable final String name,
                                 @Nullable final Component component,
                                 final float progress,
                                 @Nullable final BossBar.Color color,
                                 @Nullable final BossBar.Overlay overlay) {
    return null;
  }
}
