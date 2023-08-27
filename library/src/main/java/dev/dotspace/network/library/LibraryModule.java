package dev.dotspace.network.library;

import com.google.inject.AbstractModule;
import dev.dotspace.common.response.ResponseService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;


/**
 * Singleton library module
 */
@Log4j2
@Accessors(fluent=true)
public final class LibraryModule extends AbstractModule {

  /**
   * Service for {@link dev.dotspace.common.response.ResponseService}.
   */
  @Getter(AccessLevel.PROTECTED)
  private final @NotNull ResponseService responseService;

  /**
   * avoid access.
   */
  LibraryModule() {
    this.responseService = ResponseService
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

  /**
   * Configure module.
   */
  @Override
  protected void configure() {
    //Bind service.
    this.bind(ResponseService.class).toInstance(this.responseService);
  }
}
