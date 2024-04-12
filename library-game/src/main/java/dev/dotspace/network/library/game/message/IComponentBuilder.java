package dev.dotspace.network.library.game.message;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;


public interface IComponentBuilder {
  /**
   * Create component of string.
   *
   * @param content to convert to component.
   */
  @NotNull Component component(@Nullable final String content);

  /**
   * Create component list of string. split at '\n'.
   *
   * @param content to convert to component.
   */
  @NotNull Collection<Component> componentList(@Nullable final String content);
}
