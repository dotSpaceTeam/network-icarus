package dev.dotspace.network.node.message.text;

import dev.dotspace.network.node.message.text.element.ElementManager;
import dev.dotspace.network.node.message.text.parser.TextParser;

import java.util.regex.Pattern;


public class manint {
  private static final Pattern TAG_PATTERN =
    Pattern.compile("\\{\\{ ([A-Z]+):([A-Za-z0-9@./{}()_-]+):*([A-Za-z0-9@./{}()_-]+)* }}");

  public static ElementManager elementManager = new ElementManager();

  public static void main(String[] args) {
    new TextParser().parse("{{ KEY:SECOND:THIRD }} test");
  }


}
