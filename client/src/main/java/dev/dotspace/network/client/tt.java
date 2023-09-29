package dev.dotspace.network.client;

public class tt {

  public static void main(String[] args) throws InterruptedException {
    Client.connect("http://localhost:8443/");

    Client
        .client()
        .profileRequest()
        .getProfile("Test").get();
  }
}
