package dev.dotspace.network.library.spring;

import dev.dotspace.network.library.server.IRuntimeInfo;
import dev.dotspace.network.library.server.ImmutableRuntimeInfo;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;

import java.util.Objects;
import java.util.UUID;

@Log4j2
@Accessors(fluent = true)
public abstract class SpringRunner implements ISpringRunner {
  /**
   * See {@link ISpringRunner#id()}.
   */
  @Getter
  private final @NotNull String id = UUID.randomUUID().toString();
  /**
   * See {@link ISpringRunner#runnerType()}.
   */
  @Getter
  private final @NotNull RunnerType runnerType;
  private final @NotNull SpringApplication springApplication;

  /**
   * Create client instance with spring parameters.
   *
   * @param applicationClass class with {@link org.springframework.boot.autoconfigure.SpringBootApplication}.
   * @param args             of java runtime.
   */
  public SpringRunner(@Nullable final Class<?> applicationClass,
                      @Nullable final String[] args,
                      @Nullable final RunnerType runnerType) {
    //Null check
    Objects.requireNonNull(applicationClass);
    Objects.requireNonNull(args);
    Objects.requireNonNull(runnerType);

    this.runnerType = runnerType;

    this.springApplication = new SpringApplication(applicationClass);
    if (runnerType != RunnerType.NODE) {
      log.info("Disabling tomcat web application.");
      this.springApplication.setWebApplicationType(WebApplicationType.NONE);
    }
    log.info("Run application.");
    this.springApplication.run(args);

    log.info("Runner(id={}, type={}) loaded.", this.id, this.runnerType);

    //Print system info.
    final IRuntimeInfo runtimeInfo = ImmutableRuntimeInfo.now();
    log.info("Allocated {} cores and {} mb of ram storage.", runtimeInfo.cores(), runtimeInfo.totalMemory());
  }
}
