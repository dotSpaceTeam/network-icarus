package dev.dotspace.network.library.spigot.scoreboard;

import dev.dotspace.network.library.game.scoreboard.ISidebar;
import lombok.extern.log4j.Log4j2;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Log4j2
public final class Sidebar implements ISpigotSidebar {
  /**
   * Object to modify player.
   */
  private @Nullable FastBoard fastBoard;

  protected Sidebar(@NotNull final Player player) {
    this.fastBoard = new FastBoard(player);
    log.debug("Created sidebar for {}.", player);
  }

  /**
   * See {@link ISidebar#title()}
   */
  @Override
  public @NotNull Optional<Component> title() {
    //Modify board
    return Optional
      .ofNullable(this.fastBoard)
      .map(FastBoard::getTitle);
  }

  /**
   * See {@link ISidebar#title(Object)}
   */
  @Override
  public @NotNull ISidebar<Component> title(@Nullable Component component) {
    //Modify board
    this.fastBoard()
      .ifPresent(fastBoard -> fastBoard.updateTitle(component));
    return this;
  }

  /**
   * See {@link ISidebar#lines()}
   */
  @Override
  public @NotNull List<Component> lines() {
    //Modify board
    return this.fastBoard()
      //Get all lines.
      .map(FastBoard::getLines)
      //Else return empty list.
      .orElse(Collections.emptyList());
  }

  /**
   * See {@link ISidebar#line(int)}
   */
  @Override
  public @NotNull Optional<Component> line(int line) {
    //Modify board
    return this.fastBoard()
      .map(fastBoard -> fastBoard.getLine(line));
  }

  /**
   * See {@link ISidebar#line(int)}
   */
  @Override
  public @NotNull ISidebar<Component> line(int line,
                                           @Nullable Component component) {
    //Check if parameters are present.
    if (component == null) {
      log.warn("Given line is empty. To remove line from board use methode deleteLine(int).");
      return this;
    }

    //Modify board
    this.fastBoard()
      .ifPresent(fastBoard -> fastBoard.updateLine(line, component));
    return this;
  }

  /**
   * See {@link ISidebar#lines(Collection)}
   */
  @Override
  public @NotNull ISidebar<Component> lines(@Nullable Collection<Component> components) {
    //Check if parameters are present.
    if (components == null) {
      log.warn("Given lines are empty. To clear board use methode clear().");
      return this;
    }

    //Modify board
    this.fastBoard()
      .ifPresent(fastBoard -> fastBoard.updateLines(components));
    return this;
  }

  /**
   * See {@link ISidebar#deleteLine(int)}
   */
  @Override
  public @NotNull ISidebar<Component> deleteLine(int line) {
    //Modify board
    this.fastBoard()
      .ifPresent(fastBoard -> fastBoard.removeLine(line));
    return this;
  }

  /**
   * See {@link ISidebar#clear()}
   */
  @Override
  public @NotNull ISidebar<Component> clear() {
    this.fastBoard().ifPresent(FastBoard::updateLines);
    return this;
  }

  /**
   * See {@link ISidebar#delete()}
   */
  @Override
  public boolean delete() {
    //Check if deleted.
    if (this.deleted()) {
      return false;
    }
    //Delete and set to null.
    this.fastBoard.delete();
    this.fastBoard = null;
    return true;
  }

  /**
   * See {@link ISidebar#deleted()}
   */
  @Override
  public boolean deleted() {
    return this.fastBoard == null;
  }

  /**
   * Wrap fastBoard in {@link Optional}.
   */
  private Optional<FastBoard> fastBoard() {
    return Optional.ofNullable(this.fastBoard);
  }
}
