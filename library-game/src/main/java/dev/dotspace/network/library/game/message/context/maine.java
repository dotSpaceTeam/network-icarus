package dev.dotspace.network.library.game.message.context;

import dev.dotspace.network.client.Client;

import java.util.Locale;


public class maine {

  public static void main(String[] args) {

    Client.connect("http://localhost:8443");

    Client.client()
        .messageRequest()
        .updateMessage(Locale.GERMANY, "serv@prefix", "[Server]", true);

    Client.client()
        .messageRequest()
        .updateMessage(Locale.GERMANY,
            "serv@join",
            "{{ KEY:serv@prefix }} Der Spieler {{ PLACEHOLDER:Name }} hat den Server betreten.", true);


    System.out.println(MessageContext
        .formatOffline("Hallo {{ PLACEHOLDER:Name }}!")
        .replace("Name", "Welt")
        .complete());

    System.out.println(MessageContext
        .format("Antwort von {{ PLACEHOLDER:Name }}: {{ KEY:test }}!", Locale.GERMANY)
        .replace("Name", "Welt")
        .complete());

    MessageContext
        .key("serv@join", Locale.GERMANY)
        .replace("Name", "Te")
        .handle(s -> {
          System.out.println("Handled: "+s);
        })
        .complete()
        .ifPresent(s -> {
          System.out.println("Present: "+s);
        });


  }
}
