package dev.dotspace.network.node.message;

import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.network.library.message.IMessage;
import dev.dotspace.network.node.message.db.MessageDatabase;
import dev.dotspace.network.node.message.text.IPlaceholder;
import dev.dotspace.network.node.message.text.ITextMessage;
import dev.dotspace.network.node.message.text.ImmutableTextMessage;
import dev.dotspace.network.node.message.text.Placeholder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
@Component("messageManager")
public final class MessageManager {

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
  private static final Pattern TAG_PATTERN =
    Pattern.compile("\\{\\{ ([A-Z]+):([A-Za-z0-9@./{}()_-]+):*([A-Za-z0-9@./{}()_-]+)* }}");

  /**
   * Message database to get message from.
   */
  @Autowired
  private MessageDatabase messageDatabase;

  public @NotNull ITextMessage message(@Nullable final String message) {
    return null;
  }

  public @NotNull CompletableResponse<ITextMessage> key(@Nullable final Locale locale,
                                                        @Nullable final String key) {
    return this.messageDatabase
      .message(locale, key)
      .map(this::a);
  }

  private ITextMessage a(@NotNull final IMessage message) {
    return this.a(message.locale(), message.message(), new ArrayList<>(List.of(message.key())));
  }

  private ITextMessage a(@Nullable final Locale locale,
                         @Nullable final String plainMessage,
                         @Nullable List<String> presentKeys) {
    //Null check
    Objects.requireNonNull(locale);
    Objects.requireNonNull(plainMessage);

    String innerText = plainMessage;
    final Set<IPlaceholder<?>> placeholders = new HashSet<>();

    final Matcher matcher = TAG_PATTERN.matcher(plainMessage);

    while (matcher.find()) {
      final int groupCount = matcher.groupCount();

      if (groupCount == 2 || groupCount == 3) {
        final String tag = matcher.group().replace("{", "\\{");
        final String parameter = matcher.group(1).toUpperCase(Locale.ROOT);
        final String value = matcher.group(2);

        final String option = groupCount == 3 ? matcher.group(3) : null;

        @Nullable List<String> finalPresentKeys = presentKeys;
        switch (parameter) {
          case "KEY" -> {
            String replaceKey;

            try {
              if (presentKeys == null) {
                presentKeys = new ArrayList<>();
              }
              presentKeys.add(value);

              replaceKey = Optional
                .ofNullable(this.messageDatabase.message(locale, value).get())
                .map(IMessage::message)
                .map(s -> this.a(locale, s, finalPresentKeys))
                .map(iTextMessage -> {
                  placeholders.addAll(iTextMessage.placeholders());
                  return iTextMessage.plainText();
                })
                .orElse("");

            } catch (InterruptedException e) {
              throw new RuntimeException(e);
            }

            innerText = innerText.
              replaceFirst(matcher.group().replace("{", "\\{"), replaceKey);
          }
          case "PLACEHOLDER" -> {
            LOGGER.debug("Placeholder '{}', was found in '{}'.", value, innerText);
            /*
             * Create object to remind placeholder.
             */
            final IPlaceholder<?> placeholder = new Placeholder<>(matcher.group(), value);
            //Add placeholder.
            placeholders.add(placeholder);
          }
        }
      }
    }
    return new ImmutableTextMessage(innerText, placeholders);
  }
}
