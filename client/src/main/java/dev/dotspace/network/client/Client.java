package dev.dotspace.network.client;

import dev.dotspace.network.client.position.PositionRequest;
import dev.dotspace.network.client.profile.ProfileRequest;
import dev.dotspace.network.library.position.IPositionManipulator;
import dev.dotspace.network.library.profile.IProfileManipulator;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;

import java.util.Objects;

/**
 * Implementation for {@link IClient}.
 */
@Log4j2
public final class Client implements IClient {

  private final @NotNull SpringApplication springApplication;

  /**
   * Create client instance with spring parameters.
   *
   * @param applicationClass class with {@link org.springframework.boot.autoconfigure.SpringBootApplication}.
   * @param args             of java runtime.
   */
  public Client(@Nullable final Class<?> applicationClass,
                @Nullable final String[] args) {
    //Null check
    Objects.requireNonNull(applicationClass);
    Objects.requireNonNull(args);

    this.springApplication = new SpringApplication(applicationClass);
    log.info("Disabling tomcat web application.");
    springApplication.setWebApplicationType(WebApplicationType.NONE);
    log.info("Run application.");
    springApplication.run(args);
  }

  /**
   * See {@link IClient#profileRequest()}.
   */
  @Override
  public @NotNull IProfileManipulator profileRequest() {
    return new ProfileRequest();
  }

  /**
   * See {@link IClient#positionRequest()}.
   */
  @Override
  public @NotNull IPositionManipulator positionRequest() {
    return new PositionRequest();
  }
}
