package dev.dotspace.network.node.message.text;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dotspace.network.node.message.text.parser.TextParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class manint {
  @Autowired
  private TextParser textParser;

  public static void main(String[] args) throws JsonProcessingException {
    var a = SpringApplication.run(manint.class, args);

    TextParser parser = a.getBean(TextParser.class);
    final ITextMessage iTextMessage = parser.parse("{{ PLACEHOLDER:SECOND:THIRD }} test");

    iTextMessage
      .placeholders()
      .forEach(placeholder -> {
        System.out.println(placeholder);
        System.out.println(placeholder.code());
      });

    System.out.println(new ObjectMapper().writeValueAsString(iTextMessage));

    System.out.println(iTextMessage.formatted());

    System.out.println(iTextMessage.replace("SECOND", "TestRep").formatted());
  }

}
