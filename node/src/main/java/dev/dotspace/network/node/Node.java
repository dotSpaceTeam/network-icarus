package dev.dotspace.network.node;

import dev.dotspace.network.library.runtime.RuntimeType;
import dev.dotspace.network.library.spring.SpringRunner;
import dev.dotspace.network.node.runtime.db.RuntimeDatabase;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Log4j2
@Accessors(fluent = true)
public final class Node extends SpringRunner implements INode {

  /**
   * See {@link SpringRunner#SpringRunner(Class, String[], RuntimeType)}.
   */
  public Node(@Nullable final Class<?> applicationClass,
              @Nullable final String[] args) {
    super(applicationClass, args, RuntimeType.NODE);
    //Insert local runtime in database.
    this
      .bean(RuntimeDatabase.class)
      .ifPresent(runtimeDatabase -> runtimeDatabase.createRuntime(this.runtimeId(), this.type()));
    log.info("Initialized node.");
    instance = this;
  }

  //static
  @Getter
  @Accessors(fluent = true)
  private static INode instance;
}
