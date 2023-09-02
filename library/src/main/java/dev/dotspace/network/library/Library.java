package dev.dotspace.network.library;

import dev.dotspace.common.response.ResponseService;
import dev.dotspace.network.library.message.v2.MessageService;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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
   * Service to handle message package.
   */
  @Getter
  @Accessors(fluent=true)
  private final static @NotNull MessageService messageService;

  /**
   * Library module.
   */
  @Getter
  @Accessors(fluent=true)
  private final static @NotNull LibraryModule module = new LibraryModule();

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

    messageService = new MessageService();
  }
}
