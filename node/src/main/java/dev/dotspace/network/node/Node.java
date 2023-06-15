package dev.dotspace.network.node;

import dev.dotspace.network.library.server.IRuntimeInfo;
import dev.dotspace.network.library.server.ImmutableRuntimeInfo;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Log4j2
@Accessors(fluent = true)
public final class Node implements INode {

  /**
   * See {@link INode#id()}.
   */
  @Getter
  private final @NotNull String id = UUID.randomUUID().toString();

  //static

  @Getter
  @Accessors(fluent = true)
  private static INode instance;

  static void load() {
    log.info("Loading node...");
    instance = new Node();

    log.info("Node({}) loaded.", instance.id());

    //Print system info.
    final IRuntimeInfo runtimeInfo = ImmutableRuntimeInfo.now();
    log.info("Allocated {} cores and {} mb of ram storage.", runtimeInfo.cores(), runtimeInfo.totalMemory());
  }
}
