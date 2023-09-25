package dev.dotspace.network.library.game.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;



public final class ComponentBuilder implements IComponentBuilder {

  /**
   * See {@link IComponentBuilder#component(String)}.
   */
  @Override
  public @NotNull Component component(@Nullable String content) {
    return ComponentUtils.component(content);
  }

  /**
   * See {@link IComponentBuilder#componentList(String)}
   */
  @Override
  public @NotNull Collection<Component> componentList(@Nullable String content) {
    return ComponentUtils.componentList(content);
  }
}
