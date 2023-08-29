package dev.dotspace.network.library.spigot.self.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;


public final class Message {
  /**
   * Kick message for players.
   */
  public static final Component CLIENT_OFFLINE_KICK = MiniMessage
      .miniMessage()
      .deserialize("<red>Gameserver has lost communication, try again.");

  /**
   * Info message for players with permission {@link dev.dotspace.network.library.game.permission.Permission#OFFLINE}
   */
  public static final Component CLIENT_OFFLINE_INFO = MiniMessage
      .miniMessage()
      .deserialize("<red>Spigot Server has lost communication to master.");

  /**
   * Info message for players with permission {@link dev.dotspace.network.library.game.permission.Permission#OFFLINE}
   */
  public static final Component CLIENT_ONLINE_INFO = MiniMessage
      .miniMessage()
      .deserialize("<green>Connection back online.");
}
