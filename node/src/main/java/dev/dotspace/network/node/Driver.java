package dev.dotspace.network.node;

import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Log4j2
@SpringBootApplication
public class Driver {

  public static void main(String[] args) {
    new Node(Driver.class, args);
  }

  @Bean
  public WebMvcConfigurer webConfig() {

    {
      log.info("Created WebMvcConfigurer bean instance.");
    }

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
}
