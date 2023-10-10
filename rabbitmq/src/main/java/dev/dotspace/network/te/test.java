package dev.dotspace.network.te;

import dev.dotspace.network.library.profile.ImmutableProfile;
import dev.dotspace.network.library.profile.ProfileType;
import dev.dotspace.network.rabbitmq.IRabbitConnection;
import dev.dotspace.network.rabbitmq.RabbitConnection;
import dev.dotspace.network.rabbitmq.exception.RabbitClientAbsentException;
import dev.dotspace.network.rabbitmq.exception.RabbitIOException;

import java.nio.charset.StandardCharsets;

public class test {

  public static void main(String[] args) throws RabbitClientAbsentException, RabbitIOException {
    final IRabbitConnection rabbitClient = new RabbitConnection("user", "password", "icarus", "localhost", 5672);

    rabbitClient.createQueueIfAbsent("icarus-update-2", true, true, false, null);
    rabbitClient.createExchangeIfAbsent("icarus-broadcast", "fanout");

    rabbitClient
        .newSubscriber("icarus-broadcast", "")
        .subscribe(bytes -> {
          System.out.println("Subcriber content : "+new String(bytes));
        })
        .subscribe(ImmutableProfile.class, immutableProfile -> {
          System.out.println("Get : "+immutableProfile);
        })
        .subscribe(Test.class, test -> {
          System.out.println("Subscriber : "+test);
        });

    rabbitClient.newPublisher()
        .publish("icarus-broadcast", "icarus-update-2", "Test".getBytes(StandardCharsets.UTF_8));


    rabbitClient.newPublisher()
        .publish("icarus-broadcast", "icarus-update-2", ImmutableProfile.class,
            ImmutableProfile.of(new ImmutableProfile("Test", "Tester", ProfileType.JAVA)));


  }

  public static record Test(String value) {


    public void print() {
      System.out.println("hallu :D");
    }

  }

}
