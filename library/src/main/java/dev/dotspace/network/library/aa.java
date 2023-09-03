package dev.dotspace.network.library;

import dev.dotspace.common.response.Response;
import dev.dotspace.network.library.message.AbstractMessageComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class aa {

  public static void main(String[] args) {
    final String s = "{{ PLACEHOLDER:Test }} Der test@key funktioniert. {{ PLACEHOLDER:Test2 }} ";

    System.out.println(new TestC(s)
        .replace("Test", "Ja")
        .replace("Test2", "Nein")
        .convert());
  }

  public static class TestC extends AbstractMessageComponent {

    public TestC(@Nullable String message) {
      super(message);
    }

    @Override
    public <TYPE> @NotNull TYPE convert(@Nullable Class<TYPE> typeClass) {
      return null;
    }

    @Override
    public @NotNull <TYPE> Response<TYPE> convertAsync(@Nullable Class<TYPE> typeClass) {
      return null;
    }
  }

}
