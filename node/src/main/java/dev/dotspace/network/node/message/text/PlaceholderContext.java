package dev.dotspace.network.node.message.text;

import org.apache.logging.log4j.util.Supplier;

/**
 * Supply the context to replace on message build
 *
 * @param <TYPE> generic type of placeholder.
 */
@FunctionalInterface
public interface PlaceholderContext<TYPE> extends Supplier<TYPE> {
  /*
   * Nothing to see here :D
   */
}
