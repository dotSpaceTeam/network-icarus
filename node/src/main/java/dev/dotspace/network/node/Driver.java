package dev.dotspace.network.node;

import dev.dotspace.network.node.runtime.db.RuntimeDatabase;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class Driver {
  public static void main(String[] args) {
    new Node(SpringApplication.run(Driver.class, args));
  }
}
