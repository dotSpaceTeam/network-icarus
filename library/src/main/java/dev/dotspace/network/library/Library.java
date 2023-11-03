package dev.dotspace.network.library;

import dev.dotspace.common.response.ResponseService;
import dev.dotspace.network.library.config.ConfigService;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;


@Log4j2
public final class Library {
  /**
   * Response service to handle hole library.
   */
  @Getter
  @Accessors(fluent=true)
  private final static @NotNull ResponseService responseService;
  /**
   * Config service of library.
   */
  @Getter
  @Accessors(fluent=true)
  private final static @NotNull ConfigService configService;
  /**
   * Library module.
   */
  @Getter
  @Accessors(fluent=true)
  private final static @NotNull LibraryModule module = new LibraryModule();

  /**
   * Get parameter of system (-Dicarus-[name]).
   *
   * @param name to get parameter from.
   * @return value of parameter. {@link Optional#empty()} if key is not present.
   */
  public static @NotNull Optional<String> jvmParameter(@NotNull final String name) {
    return Optional.ofNullable(System.getProperty("icarus-"+name));
  }

  static {
    final List<String> SILENT = List.of(
        "org.springframework.web.reactive.function.client.WebClientRequestException"
    );

    responseService = ResponseService
        //Create builder
        .builder()
        //Define executor
        .executorService(Executors.newCachedThreadPool())

        //Global exception handle
        .exceptionConsumer(throwable -> {
          //Return if throwable is null -> no error.
          if (throwable == null) {
            return;
          }

          //Return if class is set to silent.
          if (SILENT.contains(throwable.getClass().getName())) {
            return;
          }

          //Else print error.
          log.warn("Error while executing response.", throwable);
        })
        //Finalize build
        .build();

    configService = new ConfigService();
  }
}
