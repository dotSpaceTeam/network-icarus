package dev.dotspace.network.library.message;

import com.google.inject.Singleton;
import dev.dotspace.network.library.message.placeholder.IPlaceholder;
import dev.dotspace.network.library.message.parser.MessageParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


@Singleton
public final class MessageService {

  public @NotNull String fromMessage(@Nullable final IMessage message) {
    //Null check
    Objects.requireNonNull(message);

    return message.message();
  }

  public @NotNull String fromComponent(@Nullable final IMessageComponent messageComponent) {
    //Null check
    Objects.requireNonNull(messageComponent);

    final AtomicReference<String> atomicReference = new AtomicReference<>(messageComponent.message());
    final MessageParser ma1 = new MessageParser();
    final Map<String, IPlaceholder<?>> placeholderMap = new HashMap<>();

    messageComponent.placeholderCollection().placeholderList().forEach(iPlaceholder -> {
      placeholderMap.put(iPlaceholder.replaceText(), iPlaceholder);
    });


    ma1.handle("PLACEHOLDER", context -> {
      final String field = context.valueField();
      final String replace = Optional
          .ofNullable(placeholderMap.get(field))
          .map(iPlaceholder -> String.valueOf(iPlaceholder.content()))
          .orElse("%"+field+"%");

      atomicReference.set(context.replace(atomicReference.get(), replace));
    });

    ma1.parse(atomicReference.get());

    return atomicReference.get();
  }


}
