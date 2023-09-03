package dev.dotspace.network.library.message;

import dev.dotspace.common.function.ThrowableSupplier;
import dev.dotspace.network.library.message.parser.MessageParser;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Accessors(fluent=true)
public abstract class AbstractMessageComponent implements IMessageComponent {

  @Getter
  private @NotNull String message;

  private final @NotNull Map<String, ThrowableSupplier<?>> placeholderMap;

  public AbstractMessageComponent(@Nullable final String message) {
    //Null check
    Objects.requireNonNull(message);

    this.message = message;
    this.placeholderMap = new HashMap<>();
  }

  @Override
  public @NotNull <REPLACE_TYPE> IMessageComponent replace(@Nullable String replaceText,
                                                           @Nullable REPLACE_TYPE content) {
    //Null check
    Objects.requireNonNull(replaceText);
    Objects.requireNonNull(content);

    //Add replace content.
    this.placeholderMap.put(replaceText, () -> content /*Create empty supplier.*/);
    return this;
  }

  @Override
  public @NotNull <REPLACE_TYPE> IMessageComponent replace(@Nullable String replaceText,
                                                           @Nullable ThrowableSupplier<REPLACE_TYPE> content) {
    //Null check
    Objects.requireNonNull(replaceText);
    Objects.requireNonNull(content);

    //Add replace content.
    this.placeholderMap.put(replaceText, content);
    return this;
  }

  /**
   * See {@link IMessageComponent#convert()}
   */
  @Override
  public @NotNull String convert() {
    this.update();
    return this.message;
  }

  protected void update() {
    //Create parser.
    final MessageParser messageParser = new MessageParser();

    //Handle placeholder
    messageParser.handle("PLACEHOLDER", context -> {
      //Return context if null.
      if (context == null) {
        return;
      }

      final String field = context.valueField();
      final String replace = Optional
          //Get content.
          .ofNullable(this.placeholderMap.get(field))
          .map(throwableSupplier -> {
            try {
              //Format placeholder
              return String.valueOf(throwableSupplier.get());
            } catch (final Throwable exception) {
              throw new RuntimeException(exception);
            }
          })
          .orElse("%"+field+"%");

      //Update message.
      this.message = context.replace(this.message, replace);
    });

    //Parse message
    messageParser.parse(this.message);
  }

}
