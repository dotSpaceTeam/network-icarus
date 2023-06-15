package dev.dotspace.network.node.message.text.parser;

import dev.dotspace.network.node.message.text.component.ITextContent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ITextParser {

  @NotNull ITextContent parse(@Nullable final String message);

}
