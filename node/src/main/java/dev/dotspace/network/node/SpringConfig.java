package dev.dotspace.network.node;

import dev.dotspace.common.SpaceObjects;
import dev.dotspace.common.response.ResponseService;
import dev.dotspace.network.library.Library;
import dev.dotspace.network.library.system.environment.ISystemEnvironment;
import dev.dotspace.network.library.system.environment.ImmutableSystemEnvironment;
import dev.dotspace.network.library.system.participant.IParticipant;
import dev.dotspace.network.library.system.participant.ImmutableParticipant;
import dev.dotspace.network.library.system.participant.ParticipantType;
import dev.dotspace.network.node.web.WebInterceptor;
import dev.dotspace.network.rabbitmq.IRabbitClient;
import dev.dotspace.network.rabbitmq.RabbitClient;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


@Configuration
@Log4j2
public class SpringConfig {
  /**
   * {@link WebInterceptor} to hook into rest-controller processing.
   */
  @Autowired
  private WebInterceptor webInterceptor;
  /**
   * Properties of build
   */
  @Autowired
  private BuildProperties buildProperties;

  /**
   * Values of application.properties
   */
  @Autowired
  private Environment environment;

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
            //Define controller pattern
            .addViewController("/")
            //Redirect to default registry.
            .setViewName("forward:/index.html");
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
  public @NotNull IParticipant participant() {
    return ImmutableParticipant.randomOfType(ParticipantType.NODE);
  }

  /**
   * Configure instance of {@link IRabbitClient}
   */
  @Bean
  public @NotNull IRabbitClient rabbitClient() {
    //Starting
    log.info("Configuring Node-Rabbitmq...");

    //Get uri
    final String rabbitUri = this.environment.getProperty("rabbitmq.uri");

    //Connect to rabbit.
    RabbitClient.connect(rabbitUri);

    //Return instance.
    return RabbitClient.client();
  }

  /**
   * Configure thread service.
   */
  @Bean
  public @NotNull Executor threadService() {
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

  /**
   * Define default locale.
   */
  @Bean
  public @NotNull Locale defaultLocale() {
    return Locale.US;
  }

  /**
   * Create system environment to pass to clients.
   */
  @Bean
  public @NotNull ISystemEnvironment systemEnvironment() {
    return new ImmutableSystemEnvironment(
        //Pass build of node.
        this.buildProperties.getVersion(),
        //Pass rabbit mq uri.
        SpaceObjects.ifAbsentUse(this.environment.getProperty("rabbitmq.uri"), () -> ""));
  }
}