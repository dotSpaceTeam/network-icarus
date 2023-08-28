package dev.dotspace.network.library;

import dev.dotspace.common.response.ResponseService;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;

@Log4j2
public final class Library {

  @Getter
  @Accessors(fluent=true)
  private final static @NotNull ResponseService responseService;

  /**
   * Library module.
   */
  @Getter
  @Accessors(fluent=true)
  private final static @NotNull LibraryModule module = new LibraryModule();

  static {
    responseService = ResponseService
        //Create builder
        .builder()
        //Define executor
        .executorService(Executors.newCachedThreadPool())

        //Global exception handle
        .exceptionConsumer(throwable -> {
          log.warn("Error while executing response.", throwable);
        })

        //Finalize build
        .build();
  }
}
