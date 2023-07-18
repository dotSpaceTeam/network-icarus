package dev.dotspace.network.node;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@Log4j2
public class SpringConfig {

  /**
   * Basic web server config.
   *
   * @return instance of config.
   */
  @Bean
  public WebMvcConfigurer webConfig() {
    log.info("Created WebMvcConfigurer bean instance.");

    return new WebMvcConfigurer() {
      @Override
      public void addViewControllers(@NotNull final ViewControllerRegistry registry) {
        registry
          .addViewController("/")
          .setViewName("forward:/index.html"); //Redirect to default registry.
        log.info("Added view controller for '/'.");
      }

      @Override
      public void addCorsMappings(@NotNull final CorsRegistry registry) {
        registry
          .addMapping("/**")
          .allowedOrigins("*"); //Allow all origins
        log.info("Allowed all origins for cors.");
      }
    };
  }

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
