package dev.dotspace.network.node;

import dev.dotspace.network.library.Library;
import dev.dotspace.network.library.message.v2.IMessageComponent;
import dev.dotspace.network.library.message.v2.ImmutableMessageComponent;
import dev.dotspace.network.library.message.v2.placeholder.PlaceholderCollection;


public class ma {

  public static void main(String[] args) {
    final String s = "{{ PLACEHOLDER:Test }} Der test@key funktioniert. {{ PLACEHOLDER:Test2 }} ";

    final IMessageComponent messageComponent = new ImmutableMessageComponent(s, new PlaceholderCollection().replace(
        "Test", "jaaa"));


    System.out.println(Library
        .messageService()
        .fromComponent(messageComponent));

  }

}
