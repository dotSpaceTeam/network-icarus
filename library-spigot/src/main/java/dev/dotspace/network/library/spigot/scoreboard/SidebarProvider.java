package dev.dotspace.network.library.spigot.scoreboard;

import dev.dotspace.network.library.game.scoreboard.GameSidebar;
import dev.dotspace.network.library.game.scoreboard.GameSidebarProvider;
import lombok.extern.log4j.Log4j2;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@org.springframework.stereotype.Component
@Log4j2
public final class SidebarProvider implements ISidebarProvider {
  /**
   * Map of player and sidebar
   */
  private final Map<Player, Sidebar> sidebarMap;

  public SidebarProvider() {
    this.sidebarMap = new ConcurrentHashMap<>();
    log.info("Initialized SidebarProvider.");
  }

  /**
   * See {@link GameSidebarProvider#sidebar(Object)}
   */
  @Override
  public @NotNull Optional<GameSidebar<Component>> sidebar(@Nullable Player player) {
    //Null check
    Objects.requireNonNull(player);

    return Optional
      //Get sidebar.
      .ofNullable(this.sidebarMap.get(player));
  }

  @Override
  public @NotNull GameSidebar<Component> create(@Nullable Player player) {
    //Null check
    Objects.requireNonNull(player);

    return this
      .sidebar(player)
      .orElseGet(() -> {
        final Sidebar sidebar = new Sidebar(player);
        this.sidebarMap.put(player, sidebar);
        log.debug("Created sidebar for {}.", player.getName());
        return sidebar;
      });
  }

  @Override
  public @NotNull Optional<GameSidebar<Component>> remove(@Nullable Player player) {
    //Null check
    Objects.requireNonNull(player);

    return Optional
      //Remove from map.
      .ofNullable(this.sidebarMap.remove(player))
      .map(sidebar -> {
        //Delete sidebar.
        sidebar.delete();
        log.debug("Deleted sidebar for {}.", player.getName());
        return sidebar;
      });
  }
}
