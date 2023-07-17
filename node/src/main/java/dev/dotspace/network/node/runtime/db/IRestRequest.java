package dev.dotspace.network.node.runtime.db;

import dev.dotspace.network.library.timestamp.ITimestamp;
import org.jetbrains.annotations.NotNull;

public interface IRestRequest extends ITimestamp {

  @NotNull String url();

  @NotNull String client();

  @NotNull String method();

  long processTime();

  boolean success();

  @NotNull String note();
}
