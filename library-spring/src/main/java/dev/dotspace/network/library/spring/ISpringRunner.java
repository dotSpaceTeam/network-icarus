package dev.dotspace.network.library.spring;

import org.jetbrains.annotations.NotNull;

/**
 * Main interface to handle {@link org.springframework.boot.SpringApplication}.
 */
public interface ISpringRunner {
  /**
   * Get type of runner.
   *
   * @return type of runner as {@link RunnerType}.
   */
  @NotNull RunnerType runnerType();
  /**
   * Id of node. String of {@link java.util.UUID}.
   *
   * @return id as {@link String}.
   */
  @NotNull String id();

}
