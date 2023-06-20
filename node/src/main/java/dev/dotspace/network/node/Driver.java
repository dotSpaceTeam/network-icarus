package dev.dotspace.network.node;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Driver {

  public static void main(String[] args) {
    new Node(Driver.class, args);
  }
}
