package dev.dotspace.network.node.message.text.parser;

import dev.dotspace.network.node.message.MessageManager;
import dev.dotspace.network.node.message.text.component.ITextContent;
import dev.dotspace.network.node.message.text.element.ElementType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public final class TextParser implements ITextParser {
  /*
   * Todo CleanUp class.
   */

  /**
   * Logger
   */
  private final static @NotNull Logger LOGGER = LogManager.getLogger(MessageManager.class);
  /**
   * Patterns to filter in message.
   */
  private static final Pattern ELEMENT_PATTERN;

  static {
    LOGGER.info("Initializing TextParser.");
    final String pattern = "\\{\\{ ([A-Z]+):([A-Za-z0-9@./{}()_-]+):*([A-Za-z0-9@./{}()_-]+)* }}";

    ELEMENT_PATTERN = Pattern.compile(pattern);
    LOGGER.info("Initialized TextParser with pattern: ({})", pattern);
  }

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  /**
   * See {@link ITextParser#parse(String)}.
   */
  @Override
  public @NotNull ITextContent parse(@Nullable String message) {
    //Null check
    Objects.requireNonNull(message);

    LOGGER.info("Parsing '{}'...", message);

    final Matcher matcher = ELEMENT_PATTERN.matcher(message);
    while (matcher.find()) {
      final int groupCount = matcher.groupCount();

      if (groupCount == 2 || groupCount == 3) {
        final String element = matcher.group().replace("{", "\\{");
        final String elementType = matcher.group(1).toUpperCase(Locale.ROOT);
        final String elementValue = matcher.group(2);
        final String elementOption = groupCount == 3 ? matcher.group(3) : null;

        final ElementType type = ElementType.valueOf(elementType);

        System.out.println(type);

        System.out.println(element);
        System.out.println(elementType);
        System.out.println(elementValue);
        System.out.println(elementOption);
      }
    }
    return null;
  }
}
