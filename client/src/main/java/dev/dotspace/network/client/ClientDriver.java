package dev.dotspace.network.client;

import dev.dotspace.network.client.profile.ProfileRequest;
import dev.dotspace.network.client.web.Client;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientDriver {

  public static ClientDriver instance;

  @Autowired
  private ProfileRequest profileRequest;

  {
    instance = this;
  }

  public static void main(String[] args) {
    final SpringApplication springApplication = new SpringApplication(ClientDriver.class);
    springApplication.setWebApplicationType(WebApplicationType.NONE);
    springApplication.run(args);

    /*  profileClient.getProfile("Test")

      .ifPresent(iProfile -> {
        System.out.println(iProfile);
      })
      .ifExceptionally(throwable -> {
        System.out.println(throwable);
      });*/

   /* profileClient.insertProfile("Test", ProfileType.JAVA)
      .ifPresent(iProfile -> {
        System.out.println(iProfile);
      })
      .ifExceptionally(throwable -> {
        System.out.println(throwable);
      });

    profileClient.getProfileAttributes("Test")
      .ifPresent(attributeList -> {
        System.out.println(attributeList);
      }).ifExceptionally(throwable -> {
        System.out.println(throwable);
      });

    profileClient.getProfileAttribute("Test", "TestKey")
      .ifPresent(attributeList -> {
        System.out.println(attributeList);
      }).ifExceptionally(throwable -> {
        System.out.println(throwable);
      });*/

    instance.profileRequest.getProfile("Test").ifPresent(iProfile -> {
      System.out.println(iProfile);
    })

    ;
  }
}
