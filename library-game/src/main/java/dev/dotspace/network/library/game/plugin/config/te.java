package dev.dotspace.network.library.game.plugin.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


public class te {

  public static void main(String[] args) throws IOException {
    var s = """
{
  "clientAutoConnect": true,
  "clientApiEndpoint": "http://localhost:8443"
}
        """;

    System.out.println(new ObjectMapper().readValue(s, PluginConfig.class).toString());
  }

}
