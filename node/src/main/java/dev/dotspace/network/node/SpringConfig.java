package dev.dotspace.network.node;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@Log4j2
public class SpringConfig {

  /**
   * Configure thread service.
   */
  @Bean
  public Executor threadService() {
    final Executor executor = Executors.newCachedThreadPool();
    log.info("Initialized thread executor bean.");
    return executor;
  }
}
