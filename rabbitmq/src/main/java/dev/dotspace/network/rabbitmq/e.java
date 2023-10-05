package dev.dotspace.network.rabbitmq;

import dev.dotspace.network.rabbitmq.exception.RabbitClientAbsentException;
import dev.dotspace.network.rabbitmq.exception.RabbitIOException;

import java.nio.charset.StandardCharsets;


public class e {

  public static void main(String[] args) throws RabbitClientAbsentException, RabbitIOException {
    final IRabbitClient rabbitClient = new RabbitClient("user", "password", "icarus", "localhost", 5672);

    rabbitClient.createQueueIfAbsent("icarus-update-2", true, true, false, null);
    rabbitClient.createExchangeIfAbsent("icarus-broadcast", "fanout");

    rabbitClient
        .newSubscriber("icarus-broadcast", "")
        .subscribe("icarus-update-2", bytes -> {
          System.out.println("Get: "+new String(bytes));
        })
        .subscribe("icarus-update-2", Test.class, test -> {
          System.out.println("Content: "+test);
        })
        .subscribe("icarus-update-2", Test.class, test -> {

          System.out.println("Content 2: "+test);
        });

    rabbitClient.newPublisher()
        .publish("icarus-broadcast", "icarus-update-2", "Test".getBytes(StandardCharsets.UTF_8));

    rabbitClient.newPublisher()
        .publish("icarus-broadcast", "icarus-update-2", Test.class, new Test("Ja"));


  }

  public static record Test(String value) {

  }

}
