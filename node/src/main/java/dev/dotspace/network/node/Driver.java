package dev.dotspace.network.node;

import lombok.extern.log4j.Log4j2;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Log4j2
@SpringBootApplication
public class Driver {
  /**
   * Main class of driver.
   *
   * @param args for jvm.
   */
  public static void main(String[] args) {
    new Node(SpringApplication.run(Driver.class, args));
  }
}
