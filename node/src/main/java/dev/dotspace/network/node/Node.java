package dev.dotspace.network.node;

import dev.dotspace.network.library.spring.RunnerType;
import dev.dotspace.network.library.spring.SpringRunner;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Log4j2
@Accessors(fluent = true)
public final class Node extends SpringRunner implements INode {

  /**
   * See {@link SpringRunner#SpringRunner(Class, String[], RunnerType)}.
   */
  public Node(@Nullable final Class<?> applicationClass,
              @Nullable final String[] args) {
    super(applicationClass, args, RunnerType.NODE);
    instance = this;
  }

  @Bean
  public WebMvcConfigurer webConfig() {
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

  //static

  @Getter
  @Accessors(fluent = true)
  private static INode instance;
}
