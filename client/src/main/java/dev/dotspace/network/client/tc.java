package dev.dotspace.network.client;

import dev.dotspace.network.client.web.ClientState;
import dev.dotspace.network.library.common.LimitStack;
import dev.dotspace.network.library.profile.ProfileType;
import lombok.SneakyThrows;

import java.util.Locale;


public class tc {

  @SneakyThrows
  public static void main(String[] args) {

    Client.connect("http://localhost:8443");

    Client.client()
        .handle(ClientState.ESTABLISHED, () -> {
          System.out.println("Connected");
        })
        .handle(ClientState.FAILED, () -> {
          System.out.println("Disconnected");
        });

    Client
        .client()
        .messageRequest()
        .createMessage(Locale.GERMANY, "test", "Test key");

/*
    new Thread(() -> {

      while (true) {
        Client
            .client()
            .profileRequest()
            .getProfile("Test")
            .ifPresent(iProfile -> {
              System.out.println(iProfile);
            })
            .ifExceptionally(throwable -> {
              System.out.println(throwable);
            })
            .ifAbsent(() -> {
              System.out.println("Absent");
            });

        try {
          Thread.sleep(2500L);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }).start();


 */
  }

}
