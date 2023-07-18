package dev.dotspace.network.node.message.text.parser;

import dev.dotspace.network.library.message.IMessage;
import dev.dotspace.network.node.message.db.MessageDatabase;
import dev.dotspace.network.node.message.text.IPlaceholder;
import dev.dotspace.network.node.message.text.ITextMessage;
import dev.dotspace.network.node.message.text.ImmutableTextMessage;
import dev.dotspace.network.node.message.text.Placeholder;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Log4j2
public final class TextParser implements ITextParser {
  /**
   * Patterns to filter in message.
   */
  private static final Pattern ELEMENT_PATTERN;

  static {
    log.info("Initializing TextParser.");
    final String pattern = "\\{\\{ ([A-Z]+):([A-Za-z0-9@./{}()_-]+):*([A-Za-z0-9@./{}()_-]+)* }}";

    ELEMENT_PATTERN = Pattern.compile(pattern);
    log.info("Initialized TextParser with pattern: ({})", pattern);
  }

  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;

  /**
   * See {@link ITextParser#parse(String)}.
   */
  @Override
  public @NotNull ITextMessage parse(@Nullable String message) {
    return this.parse(message, null);
  }

  @Override
  public @NotNull ITextMessage parse(@Nullable String message,
                                     @Nullable Locale locale) {
    //Null check
    Objects.requireNonNull(message);
    log.info("Parsing '{}'...", message);
    return new MessageSearchAlgorithm(message, null).run();
  }

  /**
   * Search in message.
   */
  @Log4j2
  private static final class MessageSearchAlgorithm {
    /**
     * Message to scan.
     */
    private @NotNull String message;
    /**
     * Locale of message.
     */
    private final @Nullable Locale locale;

    /**
     * Already found keys.
     * This list will be filled after algorithm is running.
     */
    private final @NotNull List<String> presentKeys;
    /**
     * Placeholders of message.
     * This list will be filled after algorithm is running.
     */
    private final @NotNull Set<IPlaceholder<?>> placeholders;
    /**
     * Database to get message from.
     */
    @Autowired
    private MessageDatabase messageDatabase;

    private MessageSearchAlgorithm(@NotNull final String message,
                                   @NotNull final List<String> presentKeys,
                                   @Nullable Locale locale) {
      this.message = message;
      this.locale = locale;
      this.presentKeys = presentKeys;
      this.placeholders = new HashSet<>();

      log.info("Create MessageSearchAlgorithm for message={} already found keys={}", message, presentKeys);
    }

    private MessageSearchAlgorithm(@NotNull final String message,
                                   @Nullable Locale locale) {
      this(message, new ArrayList<>(), locale);
    }

    /**
     * Run algorithm for message.
     *
     * @return parsed {@link ITextMessage}.
     */
    public @NotNull ITextMessage run() {
      final Matcher matcher = ELEMENT_PATTERN.matcher(this.message);
      while (matcher.find()) {
        final int groupCount = matcher.groupCount();

        //Ignore if not valid group count.
        if (groupCount < 2 || groupCount > 3) {
          log.warn("Illegal message to parse: {}", this.message);
          continue;
        }

        //Get total string.
        final String group = matcher.group();
        //Total string as regex compatible.
        final String element = group.replace("{", "\\{");

        //Type of element.
        final String elementType = matcher.group(1).toUpperCase(Locale.ROOT);
        //Value of element.
        final String elementValue = matcher.group(2);
        //Option, not implemented.
        final String elementOption = groupCount == 3 ? matcher.group(3) : null;

        switch (TextElement.valueOf(elementType)) {
          /*
           * Parse message for key.
           */
          case KEY -> this.handleKey(element, elementValue);

          /*
           * Parse message for placeholder.
           */
          case PLACEHOLDER -> this.handlePlaceholder(group, elementValue);
        }
      }
      return new ImmutableTextMessage(this.message, this.placeholders);
    }

    private void handleKey(@NotNull final String element,
                           @NotNull final String value) {
      if (this.messageDatabase == null) {
        log.warn("No messageDatabase present for key parse option.");
        return;
      }
      if (this.locale == null) {
        log.warn("No locale defined to key parse.");
        return;
      }

      try {
        this.presentKeys.add(value);

        final String replaceKey = Optional
          .ofNullable(this.messageDatabase.message(this.locale, value).get())
          .map(IMessage::message)
          .map(s -> new MessageSearchAlgorithm(s, this.presentKeys, this.locale).run())
          .map(iTextMessage -> {
            this.placeholders.addAll(iTextMessage.placeholders());
            return iTextMessage.plainText();
          })
          .orElse("");

        this.message = this.message.replaceFirst(element, replaceKey);
      } catch (final InterruptedException exception) {
        log.warn("Error while replacing key inside message.");
      }
    }

    private void handlePlaceholder(@NotNull final String group,
                                   @NotNull final String value) {
      log.debug("Placeholder '{}', was found in '{}'.", value, this.message);
      //Add placeholder.
      this.placeholders.add(/*Create placeholder*/ new Placeholder<>(group, value));
    }
  }
}
