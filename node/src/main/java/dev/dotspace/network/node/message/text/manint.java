package dev.dotspace.network.node.message.text;

import dev.dotspace.network.library.message.IMessage;
import dev.dotspace.network.node.message.text.element.ElementManager;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class manint {
  private static final Pattern TAG_PATTERN =
    Pattern.compile("\\{\\{ ([A-Z]+):([A-Za-z0-9@./{}()_-]+):*([A-Za-z0-9@./{}()_-]+)* }}");

  public static ElementManager elementManager = new ElementManager();

  public static void main(String[] args) {
    final Matcher matcher = TAG_PATTERN.matcher("{{ KEY:SECOND:THIRD }} test");

    while (matcher.find()) {
      final int groupCount = matcher.groupCount();

      if (groupCount == 2 || groupCount == 3) {
        final String element = matcher.group().replace("{", "\\{");
        final String elementType = matcher.group(1).toUpperCase(Locale.ROOT);
        final String elementValue = matcher.group(2);
        final String elementOption = groupCount == 3 ? matcher.group(3) : null;

        System.out.println("Test");
        System.out.println(elementManager.element(elementType, elementValue, elementOption));

        System.out.println(element);
        System.out.println(elementType);
        System.out.println(elementValue);
        System.out.println(elementOption);
      }
    }
  }

}
