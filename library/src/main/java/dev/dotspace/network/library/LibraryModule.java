package dev.dotspace.network.library;

import com.google.inject.AbstractModule;
import dev.dotspace.common.response.ResponseService;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;


/**
 * Singleton library module
 */
@Log4j2
@Accessors(fluent=true)
public final class LibraryModule extends AbstractModule {

  /**
   * Configure module.
   */
  @Override
  protected void configure() {
    //Bind service.
    this.bind(ResponseService.class).toInstance(Library.responseService());
  }
}
