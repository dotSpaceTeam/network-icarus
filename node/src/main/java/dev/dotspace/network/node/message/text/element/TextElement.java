package dev.dotspace.network.node.message.text.element;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TextElement {

  @NotNull String name();

}
