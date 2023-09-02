package dev.dotspace.network.library.message.v2;

import dev.dotspace.network.library.message.v2.placeholder.PlaceholderCollection;
import org.jetbrains.annotations.NotNull;


public interface IMessageComponent extends IMessage {

  @NotNull PlaceholderCollection placeholderCollection();

}
