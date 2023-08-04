package dev.dotspace.network.library.spigot.scoreboard;

import dev.dotspace.network.library.game.scoreboard.GameSidebar;
import lombok.extern.log4j.Log4j2;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Log4j2
public final class Sidebar implements ISidebar {
  /**
   * Object to modify player.
   */
  private @Nullable FastBoard fastBoard;

  Sidebar(@NotNull final Player player) {
    this.fastBoard = new FastBoard(player);
    log.debug("Created sidebar for {}.", player);
  }

  /**
   * See {@link GameSidebar#title()}
   */
  @Override
  public @NotNull Optional<Component> title() {
    //Modify board
    return Optional
      .ofNullable(this.fastBoard)
      .map(FastBoard::getTitle);
  }

  /**
   * See {@link GameSidebar#title(Object)}
   */
  @Override
  public @NotNull GameSidebar<Component> title(@Nullable Component component) {
    //Modify board
    this.fastBoard()
      .ifPresent(fastBoard -> fastBoard.updateTitle(component));
    return this;
  }

  /**
   * See {@link GameSidebar#lines()}
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
   * See {@link GameSidebar#line(int)}
   */
  @Override
  public @NotNull Optional<Component> line(int line) {
    //Modify board
    return this.fastBoard()
      .map(fastBoard -> fastBoard.getLine(line));
  }

  /**
   * See {@link GameSidebar#line(int)}
   */
  @Override
  public @NotNull GameSidebar<Component> line(int line,
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
   * See {@link GameSidebar#lines(Collection)}
   */
  @Override
  public @NotNull GameSidebar<Component> lines(@Nullable Collection<Component> components) {
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
   * See {@link GameSidebar#deleteLine(int)}
   */
  @Override
  public @NotNull GameSidebar<Component> deleteLine(int line) {
    //Modify board
    this.fastBoard()
      .ifPresent(fastBoard -> fastBoard.removeLine(line));
    return this;
  }

  /**
   * See {@link GameSidebar#clear()}
   */
  @Override
  public @NotNull GameSidebar<Component> clear() {
    this.fastBoard().ifPresent(FastBoard::updateLines);
    return this;
  }

  /**
   * See {@link GameSidebar#delete()}
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
   * See {@link GameSidebar#deleted()}
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
