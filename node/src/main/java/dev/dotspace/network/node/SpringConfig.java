package dev.dotspace.network.node;

import dev.dotspace.common.response.ResponseService;
import dev.dotspace.network.library.Library;
import dev.dotspace.network.library.system.IParticipant;
import dev.dotspace.network.library.system.ImmutableParticipant;
import dev.dotspace.network.library.system.ParticipantType;
import dev.dotspace.network.node.web.WebInterceptor;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


@Configuration
@Log4j2
public class SpringConfig {

  @Autowired
  private WebInterceptor webInterceptor;

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

      /**
       * There to chain into request.
       */
      @Override
      public void addInterceptors(@NotNull final InterceptorRegistry registry) {
        registry.addInterceptor(SpringConfig.this.webInterceptor);
      }
    };
  }

  /**
   * Configure openapi.
   */
  @Bean
  public OpenAPI customOpenAPI(
      @Value("${api-swagger-title}") @NotNull final String apiTitle,
      @Value("${api-swagger-description}") @NotNull final String apiDescription,
      @Value("${api-swagger-version}") @NotNull final String apiVersion
  ) {
    return new OpenAPI()
        //Create empty component.
        .components(new Components())
        //Set info.
        .info(new Info()
            .title(apiTitle)
            .description(apiDescription)
            .version(apiVersion));
  }

  /**
   * Configure {@link IParticipant}
   */
  @Bean
  public IParticipant participant() {
    return ImmutableParticipant.randomOfType(ParticipantType.NODE);
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

  /**
   * Configure responseService bean.
   */
  @Bean
  public ResponseService responseService() {
    return Library.responseService();
  }


}
