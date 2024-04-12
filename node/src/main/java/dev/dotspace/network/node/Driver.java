package dev.dotspace.network.node;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Log4j2
@EntityScan("dev.dotspace.network.node")
@EnableJpaRepositories(basePackages="dev.dotspace.network.node")
@SpringBootApplication
public class Driver {
  /**
   * Main class of driver.
   *
   * @param args for jvm.
   */
  public static void main(String[] args) {
    SpringApplication.run(Driver.class, args);
  }
}
