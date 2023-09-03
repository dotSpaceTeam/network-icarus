package dev.dotspace.network.library.message;

import dev.dotspace.network.library.message.placeholder.PlaceholderCollection;
import org.jetbrains.annotations.NotNull;


public interface IMessageComponent extends IMessage {

  @NotNull PlaceholderCollection placeholderCollection();

}
