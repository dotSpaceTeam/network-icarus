package dev.dotspace.network.te;

import dev.dotspace.network.library.profile.ImmutableProfile;
import dev.dotspace.network.library.profile.ProfileType;
import dev.dotspace.network.rabbitmq.RabbitClient;


public class test {

  public static void main(String[] args) throws Exception {


    // final IRabbitClient rabbitClient = new RabbitClient("user", "password", "icarus", "localhost", 5672);

//    rabbitClient.createQueueIfAbsent("icarus-update-2", true, true, false, null);
    //   rabbitClient.createExchangeIfAbsent("icarus-broadcast", "fanout");

    RabbitClient.connect("amqp://user:password@localhost:5672/icarus");

    Thread.sleep(5000L);

    RabbitClient
        .client()
        .newSubscriber(RabbitClient.fanoutExchange(), "")
        .subscribe(ImmutableProfile.class, payload -> {
          System.out.println("Headers: "+payload.headers());
          System.out.println("Paylaod: "+payload.payload());
        });

    RabbitClient
        .client()
        .newPublisher()
        .publish(RabbitClient.fanoutExchange(), "", ImmutableProfile.class,
            ImmutableProfile.of(new ImmutableProfile("Test", "Tester", ProfileType.JAVA)), null);
  }
}
