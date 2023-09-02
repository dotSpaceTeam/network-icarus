package dev.dotspace.network.library.message.v2;

import dev.dotspace.network.library.message.v2.placeholder.PlaceholderCollection;
import org.jetbrains.annotations.NotNull;


public interface IMessageComponent {

  @NotNull String plain();

  @NotNull IMessage plainMessage();

  @NotNull String formatted();

  @NotNull IMessage formattedMessage();

  @NotNull PlaceholderCollection placeholderCollection();

}
