package dev.dotspace.network.node;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class Driver {

  public static void main(String[] args) {
    new Node(Driver.class, args);
  }
}
