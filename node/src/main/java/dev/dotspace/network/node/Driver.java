package dev.dotspace.network.node;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Driver {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Driver.class, args);
  }

  @Bean
  public WebMvcConfigurer corsConfig() {
    return new WebMvcConfigurer() {
      @Override
      public void addViewControllers(@NotNull final ViewControllerRegistry registry) {
        registry
          .addViewController("/")
          .setViewName("forward:/index.html"); //Redirect to default registry.
      }

      @Override
      public void addCorsMappings(@NotNull final CorsRegistry registry) {
        registry
          .addMapping("/**")
          .allowedOrigins("*"); //Allow all origins
      }
    };
  }

}
