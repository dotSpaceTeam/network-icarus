package dev.dotspace.network.node;

import org.jetbrains.annotations.NotNull;

public interface INode {
  /**
   * Id of node. String of {@link java.util.UUID}.
   *
   * @return id as {@link String}.
   */
  @NotNull String id();
}
