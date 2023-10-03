package dev.dotspace.network.node;

import dev.dotspace.network.library.system.IParticipant;
import dev.dotspace.network.library.system.ISystem;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@Log4j2
@Accessors(fluent=true)
public final class Node implements INode {
  /**
   * Spring context.
   */
  @Autowired
  private ConfigurableApplicationContext applicationContext;

  public Node() {
    instance = this;
  }

  @PostConstruct
  private void init() {
    //--
  }

  @PreDestroy
  private void shutdown() {
    //--
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
          final String message = "No bean for class='"+beanName+"' found!";
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

  /**
   * See {@link ISystem#participant()}
   */
  @Override
  public @NotNull IParticipant participant() {
    //Get from bean
    return this.beanElseThrow(IParticipant.class);
  }

  //static
  @Getter
  @Accessors(fluent=true)
  private static INode instance;
}
