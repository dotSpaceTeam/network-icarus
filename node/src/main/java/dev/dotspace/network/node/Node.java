package dev.dotspace.network.node;

import dev.dotspace.network.library.runtime.IRuntime;
import dev.dotspace.network.library.runtime.ImmutableRuntime;
import dev.dotspace.network.library.runtime.RuntimeType;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.ResponseExtractor;

import java.util.Objects;
import java.util.Optional;

@Log4j2
@Accessors(fluent = true)
public final class Node implements INode {
  /**
   * Runtime info.
   */
  private final @NotNull IRuntime runtime;

  /**
   * Spring context.-
   */
  private final @NotNull ConfigurableApplicationContext applicationContext;

  public Node(@Nullable final ConfigurableApplicationContext applicationContext) {
    //Null check
    Objects.requireNonNull(applicationContext);

    //Runtime
    this.runtime = ImmutableRuntime.randomOfType(RuntimeType.NODE);
    //Insert local runtime in database.

    this.applicationContext = applicationContext;

    log.info("Node is running under id={}.", this.runtime.runtimeId());
    instance = this;
  }

  /**
   * See {@link INode#bean(Class)}.
   */
  @Override
  public @NotNull <T> Optional<T> bean(@Nullable Class<T> beanClass) {
    return Optional
      //Wrap in optional
      .ofNullable(beanClass)
      //Get bean of class.
      .map(this.applicationContext::getBean);
  }

  @Override
  public @NotNull <T> T beanElseThrow(@Nullable Class<T> beanClass) {
    return this.bean(beanClass)
      .orElseThrow(() -> {
        final String beanName = Optional.ofNullable(beanClass).map(Class::getSimpleName).orElse(null);
        final String message = "No bean for class='" + beanName + "' found!";
        log.error(message);
        return new NullPointerException(message);
      });
  }

  /**
   * See {@link INode#executeEvent(Object)}.
   */
  @Override
  public @NotNull INode executeEvent(@Nullable Object object) {
    //Null check.
    if (object == null) {
      log.warn("No event to execute.");
      return this;
    }

    //Run event.
    this.applicationContext.publishEvent(object);
    log.debug("Executed event {}.", object.getClass().getName());
    return this;
  }

  //static
  @Getter
  @Accessors(fluent = true)
  private static INode instance;
}
