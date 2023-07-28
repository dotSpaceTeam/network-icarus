package dev.dotspace.network.client;

import dev.dotspace.network.client.position.PositionRequest;
import dev.dotspace.network.client.profile.ProfileRequest;
import dev.dotspace.network.library.position.IPositionManipulator;
import dev.dotspace.network.library.profile.IProfileManipulator;
import dev.dotspace.network.library.runtime.RuntimeType;
import dev.dotspace.network.library.spring.SpringRunner;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Implementation for {@link IClient}.
 */
@Log4j2
public final class Client extends SpringRunner implements IClient {

  private static @Nullable Client instance;

  /**
   * Create client instance with spring parameters.
   *
   * @param applicationClass class with {@link org.springframework.boot.autoconfigure.SpringBootApplication}.
   * @param args             of java runtime.
   */
  public Client(@Nullable final Class<?> applicationClass,
                @Nullable final String[] args) {
    super(applicationClass, args, RuntimeType.CLIENT);
    instance = this;
  }

  /**
   * See {@link IClient#profileRequest()}.
   */
  @Override
  public @NotNull IProfileManipulator profileRequest() {
    return this.beanElseThrow(ProfileRequest.class);
  }

  /**
   * See {@link IClient#positionRequest()}.
   */
  @Override
  public @NotNull IPositionManipulator positionRequest() {
    return this.beanElseThrow(PositionRequest.class);
  }

  //static

  /**
   * Get instance of client.
   */
  public static @NotNull Client instance() {
    return Objects.requireNonNull(instance, "No client present!");
  }
}
