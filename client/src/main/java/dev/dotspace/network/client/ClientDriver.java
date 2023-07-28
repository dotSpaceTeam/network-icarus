package dev.dotspace.network.client;

import dev.dotspace.network.library.profile.IProfile;
import dev.dotspace.network.library.profile.ProfileType;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@Log4j2
@SpringBootApplication
public class ClientDriver {
  /**
   * Main method for driver.
   *
   * @param args given by user.
   */
  public static void main(String[] args) {
    new Client(ClientDriver.class, args);

    testclas.T();
  }
}
