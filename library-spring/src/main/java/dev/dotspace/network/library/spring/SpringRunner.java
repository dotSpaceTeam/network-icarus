package dev.dotspace.network.library.spring;

import dev.dotspace.network.library.runtime.RuntimeType;
import dev.dotspace.network.library.server.IHardwareInfo;
import dev.dotspace.network.library.server.ImmutableHardwareInfo;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Accessors(fluent = true)
public abstract class SpringRunner implements ISpringRunner {
  /**
   * See {@link ISpringRunner#runtimeId()}.
   */
  @Getter
  private final @NotNull String runtimeId = UUID.randomUUID().toString();
  /**
   * See {@link ISpringRunner#type()} ()}.
   */
  @Getter
  private final @NotNull RuntimeType type;
  private final @NotNull SpringApplication springApplication;
  private final @NotNull ConfigurableApplicationContext applicationContext;

  /**
   * Create client instance with spring parameters.
   *
   * @param applicationClass class with {@link org.springframework.boot.autoconfigure.SpringBootApplication}.
   * @param args             of java runtime.
   */
  public SpringRunner(@Nullable final Class<?> applicationClass,
                      @Nullable final String[] args,
                      @Nullable final RuntimeType type) {
    //Null check
    Objects.requireNonNull(applicationClass);
    Objects.requireNonNull(args);
    Objects.requireNonNull(type);

    this.type = type;

    //Define spring application.
    this.springApplication = new SpringApplication(applicationClass);

    //Disable webserver if absent.
    if (type != RuntimeType.NODE) {
      log.info("Disabling tomcat web application.");
      this.springApplication.setWebApplicationType(WebApplicationType.NONE);
    }

    log.info("Run application.");
    this.applicationContext = this.springApplication.run(args);

    log.info("Runner(id={}, type={}) loaded.", this.runtimeId, this.type);

    //Print system info.
    final IHardwareInfo hardwareInfo = ImmutableHardwareInfo.get();
    log.info("Allocated {} cores and {} mb of ram storage.", hardwareInfo.cores(), hardwareInfo.memory());
  }

  /**
   * See {@link ISpringRunner#bean(Class)}.
   */
  @Override
  public @NotNull <T> Optional<T> bean(@Nullable Class<T> beanClass) {
    return Optional
      //Wrap in optional
      .ofNullable(beanClass)
      //Get bean of class.
      .map(this.applicationContext::getBean);
  }

  /**
   * See {@link ISpringRunner#executeEvent(Object)}.
   */
  @Override
  public @NotNull ISpringRunner executeEvent(@Nullable Object object) {
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
}
