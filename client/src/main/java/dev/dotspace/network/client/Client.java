package dev.dotspace.network.client;

import dev.dotspace.network.client.position.PositionRequest;
import dev.dotspace.network.client.profile.ProfileRequest;
import dev.dotspace.network.library.position.IPositionManipulator;
import dev.dotspace.network.library.profile.IProfileManipulator;
import dev.dotspace.network.library.spring.RunnerType;
import dev.dotspace.network.library.spring.SpringRunner;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Implementation for {@link IClient}.
 */
@Log4j2
public final class Client extends SpringRunner implements IClient {

  /**
   * Create client instance with spring parameters.
   *
   * @param applicationClass class with {@link org.springframework.boot.autoconfigure.SpringBootApplication}.
   * @param args             of java runtime.
   */
  public Client(@Nullable final Class<?> applicationClass,
                @Nullable final String[] args) {
    super(applicationClass, args, RunnerType.CLIENT);
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
