package dev.dotspace.network.node.message.text;

import dev.dotspace.network.node.message.text.parser.TextParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class manint {

  private static manint z;

  @Autowired
  private TextParser textParser;

  public static void main(String[] args) {
    SpringApplication.run(manint.class, args);
    z.te();
  }

  {
    z = this;
  }
  public void te() {
    this.textParser.parse("{{ KEY:SECOND:THIRD }} test");
  }


}
