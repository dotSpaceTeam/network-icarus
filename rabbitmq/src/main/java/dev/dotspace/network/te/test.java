package dev.dotspace.network.te;

import dev.dotspace.network.library.profile.ImmutableProfile;
import dev.dotspace.network.library.profile.ProfileType;
import dev.dotspace.network.rabbitmq.RabbitClient;
import dev.dotspace.network.rabbitmq.exception.RabbitClientAbsentException;
import dev.dotspace.network.rabbitmq.exception.RabbitIOException;

import java.nio.charset.StandardCharsets;


public class test {

  public static void main(String[] args) throws RabbitClientAbsentException, RabbitIOException {


    // final IRabbitClient rabbitClient = new RabbitClient("user", "password", "icarus", "localhost", 5672);

//    rabbitClient.createQueueIfAbsent("icarus-update-2", true, true, false, null);
    //   rabbitClient.createExchangeIfAbsent("icarus-broadcast", "fanout");

    RabbitClient.connect("amqp://user:password@localhost:5672/icarus");

    RabbitClient
        .client()
        .newSubscriber("icarus-broadcast", "")
        .subscribe(bytes -> {
          System.out.println("Subcriber content : "+new String(String.valueOf(bytes)));
        })
        .subscribe(ImmutableProfile.class, payload -> {
          System.out.println("Headers: "+payload.headers());
          System.out.println("Paylaod: "+payload.payload());
        });

    RabbitClient
        .client()
        .newPublisher()
        .publish("icarus-broadcast", "icarus-update-2", "Test".getBytes(StandardCharsets.UTF_8), null);

    RabbitClient
        .client()
        .newPublisher()
        .publish("icarus-broadcast", "icarus-update-2", ImmutableProfile.class,
            ImmutableProfile.of(new ImmutableProfile("Test", "Tester", ProfileType.JAVA)), null);
  }

  public static record Test(String value) {


    public void print() {
      System.out.println("hallu :D");
    }

  }

}
