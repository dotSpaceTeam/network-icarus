package dev.dotspace.network.node;

import dev.dotspace.common.response.ResponseService;
import dev.dotspace.network.library.Library;
import dev.dotspace.network.library.system.IParticipant;
import dev.dotspace.network.library.system.ImmutableParticipant;
import dev.dotspace.network.library.system.ParticipantType;
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

  /*
  @Bean(name="dataSource")
  public DataSource dataSource() {
    log.info("Registering HikariDataSource bean.");
    HikariDataSource ds = new HikariDataSource();

    ds.setDataSourceClassName(this.environment.getProperty("spring.datasource.driver-class-name"));
    //ds.setMinimumIdle(environment.getRequiredProperty("hikari.minimumIdle", Integer.class));


    ds.setJdbcUrl(this.environment.getProperty("spring.datasource.url"));
    ds.setUsername(this.environment.getRequiredProperty("spring.datasource.username"));
    ds.setPassword(this.environment.getRequiredProperty("spring.datasource.password"));

    return ds;
  }



  // EntityManagerFactory
  @Bean(name="entityManagerFactory")
  public EntityManagerFactory entityManagerFactory() {
    log.info("Registering EntityManagerFactory bean.");
    JpaVendorAdapter hibernateJpavendorAdapter = new HibernateJpaVendorAdapter();
    JpaDialect hibernateJpaDialect = new HibernateJpaDialect();

    LocalContainerEntityManagerFactoryBean emfBean =
        new LocalContainerEntityManagerFactoryBean();
    emfBean.setJpaVendorAdapter(hibernateJpavendorAdapter);
    emfBean.setJpaDialect(hibernateJpaDialect);
  //  emfBean.setPersistenceUnitName(PERSISTENCE_UNIT);
    emfBean.setDataSource(dataSource());
    emfBean.afterPropertiesSet();
    return emfBean.getObject();
  }


  // TransactionManager
  @Bean(name="transactionManager")
  public PlatformTransactionManager transactionManager() {
    log.info("Registering JpaTransactionManager bean.");

    JpaTransactionManager txManager = new JpaTransactionManager();
    EntityManagerFactory emf = entityManagerFactory();
    txManager.setEntityManagerFactory(emf);
    txManager.setDataSource(dataSource());
    return txManager;
  }*/
}