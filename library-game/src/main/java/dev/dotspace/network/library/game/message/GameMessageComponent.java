package dev.dotspace.network.library.game.message;

import dev.dotspace.common.response.Response;
import dev.dotspace.network.library.message.AbstractMessageComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;


public final class GameMessageComponent extends AbstractMessageComponent<Component> {
  /**
   * Get mini message. Format string to {@link Component} using {@link MiniMessage}.
   */
  private final static MiniMessage MINI_MESSAGE;
  /**
   * Fallback {@link Component} if conversion fails.
   */
  private final static Component FALLBACK;

  static {
    MINI_MESSAGE = MiniMessage.miniMessage();
    FALLBACK = Component.text("<red>Error while converting message.");
  }

  public GameMessageComponent(@NotNull Supplier<String> supplier) {
    super(supplier);
  }

  @Override
  public @NotNull Response<Component> component(@Nullable String message) {
    return null;
  }
}
