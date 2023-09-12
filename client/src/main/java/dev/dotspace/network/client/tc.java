package dev.dotspace.network.client;

import lombok.SneakyThrows;

import java.util.Locale;


public class tc {

  @SneakyThrows
  public static void main(String[] args) {

    Client.enable();

    Client.client()
        .messageRequest()
        .updateMessage(Locale.GERMANY, "test@message@wow", "{{ KEY:prefix }} Es hat einfach funktioniert")
        .get();

    Client
        .client()
        .messageRequest()
        .formatString("{{ KEY:prefix }} Diese nachricht sollte eienen prefix besitzen. {{ KEY:prefix2 }}")
        .ifPresent(iMessage -> {
          System.out.println(iMessage.message());
        });

    Client
        .client()
        .messageRequest()
        .getMessage(Locale.GERMANY, "test@message@wow")
        .ifPresent(iMessage -> {
          System.out.println(iMessage.message());
        });

  }

}
