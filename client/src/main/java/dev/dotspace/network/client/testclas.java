package dev.dotspace.network.client;

import dev.dotspace.network.library.profile.ProfileType;

import java.util.UUID;

public class testclas {

  public static void T() {
    final UUID profileId = UUID.randomUUID();

    Client
      .instance()
      .profileRequest()
      .createProfile(profileId.toString(), ProfileType.JAVA)
      .ifPresent(iProfile -> {
        System.out.println("Present");
        Client
          .instance()
          .profileRequest()
          .getProfile(profileId.toString())
          .ifPresent(iProfile1 -> {
            System.out.println(iProfile1);
          });
      })
      .ifAbsent(() -> {
        System.out.println(profileId + " is null.");
      })
      .ifExceptionally(throwable -> {
        System.out.println("Error while " + profileId + " -> " + throwable);
      });
  }

}
