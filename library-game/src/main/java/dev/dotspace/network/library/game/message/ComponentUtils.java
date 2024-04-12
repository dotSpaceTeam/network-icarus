package dev.dotspace.network.library.game.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public final class ComponentUtils {
  /**
   * Character to split line
   */
  private final static @NotNull String LINE_BREAK = "\n";
  /**
   * Instance of miniMessage
   */
  private final static MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

  public static @NotNull Component component(@Nullable String content) {
    //Null check
    Objects.requireNonNull(content);

    return MINI_MESSAGE.deserialize(content);
  }


  public static @NotNull Component waitingComponent() {
    return MINI_MESSAGE.deserialize("<gray>...");
  }

  public static @NotNull Collection<Component> componentList(@Nullable String content) {
    //Null check
    Objects.requireNonNull(content);

    return Arrays.stream(content.split(LINE_BREAK))
        //Map string to component.
        .map(MINI_MESSAGE::deserialize)
        //To list.
        .toList();
  }

  public static @NotNull List<Component> waitingComponentList() {
    return Collections.singletonList(waitingComponent());
  }
}
