package dev.dotspace.network.library.message.v2.parser;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.dotspace.network.library.common.EditiableObject;
import dev.dotspace.network.library.message.v2.IMessageComponent;
import dev.dotspace.network.library.message.v2.ImmutableMessageComponent;
import dev.dotspace.network.library.message.v2.placeholder.PlaceholderCollection;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Log4j2
public final class MessageParser implements IMessageParser {

  private final @NotNull Multimap<TextElement, ParserContext> parserContextMultimap;

  public MessageParser() {
    this.parserContextMultimap = HashMultimap.create();
  }

  /**
   * See {@link IMessageParser#parse(String)}.
   */
  @Override
  public @NotNull IMessageComponent parse(@Nullable String message) {
    return this.parse(message, null);
  }

  @Override
  public @NotNull IMessageComponent parse(@Nullable String message,
                                          @Nullable Locale locale) {
    //Null check
    Objects.requireNonNull(message);
    log.info("Parsing '{}'...", message);
    return new MessageSearchAlgorithm(this, message, null).run();
  }

  @Override
  public @NotNull IMessageParser handle(@Nullable TextElement textElement,
                                        @Nullable ParserContext parserContext) {
    //Null check
    Objects.requireNonNull(textElement);
    Objects.requireNonNull(parserContext);

    this.parserContextMultimap.put(textElement, parserContext);

    return this;
  }


  /**
   * Search in message.
   */
  @Log4j2
  private static final class MessageSearchAlgorithm {
    /**
     * Patterns to filter in message.
     */
    private static final Pattern ELEMENT_PATTERN;

    static {
      log.info("Initializing Pattern.");
      final String pattern = "\\{\\{ ([A-Z]+):([A-Za-z0-9@./{}()_-]+):*([A-Za-z0-9@./{}()_-]+)* }}";

      ELEMENT_PATTERN = Pattern.compile(pattern);
      log.info("Initialized with pattern: ({})", pattern);
    }

    private final @NotNull MessageParser parser;

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
    private final @NotNull PlaceholderCollection placeholderCollection;

    private MessageSearchAlgorithm(@NotNull final MessageParser parser,
                                   @NotNull final String message,
                                   @NotNull final List<String> presentKeys,
                                   @Nullable Locale locale) {
      this.parser = parser;
      this.message = message;
      this.locale = locale;
      this.presentKeys = presentKeys;
      this.placeholderCollection = new PlaceholderCollection();

      log.info("Create MessageSearchAlgorithm for message={} already found keys={}", message, presentKeys);
    }

    private MessageSearchAlgorithm(@NotNull final MessageParser parser,
                                   @NotNull final String message,
                                   @Nullable Locale locale) {
      this(parser, message, new ArrayList<>(), locale);
    }

    /**
     * Run algorithm for message.
     *
     * @return parsed {@link IMessageComponent}.
     */
    public @NotNull IMessageComponent run() {
      final Matcher matcher = ELEMENT_PATTERN.matcher(this.message);
      while (matcher.find()) {
        final int groupCount = matcher.groupCount();

        //Ignore if not valid group count.
        if (groupCount<2 || groupCount>3) {
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

        System.out.println("Found "+elementType);

        final TextElement textElement = TextElement.valueOf(elementType);

        final EditiableObject<String> editiableObject = new EditiableObject<>(this.message);
        this.parser.parserContextMultimap
            .get(textElement)
            .forEach(parserContext -> {
              parserContext.accept(group, textElement, elementValue, elementOption, this.locale, editiableObject,
                  this.placeholderCollection);
            });

        this.message = editiableObject.type() == null ? "" : editiableObject.type();
      }
      return new ImmutableMessageComponent(this.message, this.message, this.placeholderCollection);
    }

    private void handleKey(@NotNull final String element,
                           @NotNull final String value) {
   /*   if (this.messageDatabase == null) {
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

    */
    }

    private void handlePlaceholder(@NotNull final String group,
                                   @NotNull final String value) {

    }
  }
  //static

  public static @NotNull MessageParser simple() {
    final MessageParser messageParser = new MessageParser();

    messageParser
        .handle(TextElement.PLACEHOLDER, (tag, textElement, value, option, locale, text, placeholderCollection) -> {
          log.debug("Placeholder '{}', was found in '{}'.", value, text.type());
          //Add placeholder.
          placeholderCollection.replace(/*Create placeholder*/tag, value);
        });

    return messageParser;
  }
}
