package dev.dotspace.network.library.message.parser;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class MessageParser implements IMessageParser {
  /**
   * Patterns to filter in message.
   */
  private static final Pattern ELEMENT_PATTERN;

  static {
    final String pattern = "\\{\\{ ([A-Z]+):([A-Za-z0-9@./{}()_-]+):*([A-Za-z0-9@./{}()_-]+)* }}";

    ELEMENT_PATTERN = Pattern.compile(pattern);
  }

  private final @NotNull Multimap<String, MatcherConsumer> consumerMultimap;

  public MessageParser() {
    this.consumerMultimap = HashMultimap.create();
  }

  @Override
  public @NotNull IMessageParser handle(@Nullable final String content,
                                        @Nullable final MatcherConsumer consumer) {
    //Null check
    Objects.requireNonNull(content);
    Objects.requireNonNull(consumer);

    this.consumerMultimap.put(content, consumer);
    return this;
  }

  @Override
  public void parse(@Nullable final String text) {
    //Return if text is null.
    if (text == null) {
      return;
    }

    final Matcher matcher = ELEMENT_PATTERN.matcher(text);
    while (matcher.find()) {
      final int groupCount = matcher.groupCount();

      //Ignore if not valid group count.
      if (groupCount<2 || groupCount>3) {
        continue;
      }

      //Get total string.
      final String group = matcher.group();
      //Total string as regex compatible.

      //Type of element.
      final String elementType = matcher.group(1).toUpperCase(Locale.ROOT);
      //Value of element.
      final String elementValue = matcher.group(2);
      //Option, not implemented.
      final String elementOption = groupCount == 3 ? matcher.group(3) : null;

      final MatcherContext context = new MatcherContext(group, elementType, elementValue, elementOption);
      this.consumerMultimap
          .get(elementType)
          .forEach(contextConsumer -> {
            try {
              contextConsumer.accept(context);
            } catch (final Throwable error) {
              throw new RuntimeException(error);
            }
          });
    }
  }
}
