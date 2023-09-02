package dev.dotspace.network.library.message.v2.parser;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Ma {
  /**
   * Patterns to filter in message.
   */
  private static final Pattern ELEMENT_PATTERN;

  static {
    final String pattern = "\\{\\{ ([A-Z]+):([A-Za-z0-9@./{}()_-]+):*([A-Za-z0-9@./{}()_-]+)* }}";

    ELEMENT_PATTERN = Pattern.compile(pattern);
  }

  private final @NotNull Multimap<String, func> consumerMultimap;

  public Ma() {
    this.consumerMultimap = HashMultimap.create();
  }

  public @NotNull Ma handle(@Nullable final String content,
                            @Nullable final func contextConsumer) {
    //Null check
    Objects.requireNonNull(content);
    Objects.requireNonNull(contextConsumer);

    this.consumerMultimap.put(content, contextConsumer);
    return this;
  }


  public void parse(@Nullable final String text) {
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

      final Context context = new Context(group, elementType, elementValue, elementOption);
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

  public static record Context(@NotNull String tag,
                               @NotNull String typeField,
                               @NotNull String valueField,
                               @Nullable String optionField
  ) {
    /**
     * @param text
     * @param content
     * @return
     */
    public @NotNull String replaceFirst(@NotNull String text,
                                        @NotNull String content) {
      return text.replaceFirst(this.tag.replace("{", "\\{"), content);
    }

    /**
     * @param text
     * @param content
     * @return
     */
    public String replace(@NotNull String text,
                          @NotNull String content) {
      return text.replace(this.tag, content);
    }

  }

  public interface func {

    void accept(@NotNull final Context context) throws Exception;

  }

}
