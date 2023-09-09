package dev.dotspace.network.library.message;

import dev.dotspace.common.function.ThrowableSupplier;
import dev.dotspace.network.library.message.parser.MessageParser;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;


@Accessors(fluent=true)
public abstract class AbstractMessageComponent<COMPONENT> implements IMessageComponent<COMPONENT> {

  private final @NotNull Supplier<String> supplier;
  private final @NotNull Map<String, ThrowableSupplier<?>> placeholderMap;

  public AbstractMessageComponent(@NotNull final Supplier<String> supplier) {
    this.supplier = supplier;
    this.placeholderMap = new HashMap<>();
  }

  @Override
  public @NotNull <REPLACE_TYPE> IMessageComponent<COMPONENT> replace(@Nullable String replaceText,
                                                                      @Nullable REPLACE_TYPE content) {
    //Null check
    Objects.requireNonNull(replaceText);
    Objects.requireNonNull(content);

    //Add replace content.
    this.placeholderMap.put(replaceText, () -> content /*Create empty supplier.*/);
    return this;
  }

  @Override
  public @NotNull <REPLACE_TYPE> IMessageComponent<COMPONENT> replace(@Nullable String replaceText,
                                                                      @Nullable ThrowableSupplier<REPLACE_TYPE> content) {
    //Null check
    Objects.requireNonNull(replaceText);
    Objects.requireNonNull(content);

    //Add replace content.
    this.placeholderMap.put(replaceText, content);
    return this;
  }

  protected @NotNull String process() {
    final AtomicReference<String> reference = new AtomicReference<>(this.supplier.get());
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
      reference.set(context.replace(reference.get(), replace));
    });

    //Parse message
    messageParser.parse(reference.get());

    return reference.get();
  }

}
