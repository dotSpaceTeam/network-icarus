package dev.dotspace.network.node.message.text;

import dev.dotspace.network.node.message.text.parser.TextParser;


public class manint {
  public static void main(String[] args) {
    new TextParser().parse("{{ KEY:SECOND:THIRD }} test");
  }


}
