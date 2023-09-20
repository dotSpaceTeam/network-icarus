package dev.dotspace.network.library.common;

import java.util.Stack;


/**
 * Child of stack -> limited to amount.
 *
 * @param <T> generic type of stack elements.
 */
public class LimitStack<T> extends Stack<T> {
  /**
   * Maximum amount of elements.
   */
  private final int maxStackHeight;

  public LimitStack(int size) {
    super();
    this.maxStackHeight = size;
  }

  /**
   * See {@link Stack#push(Object)}.
   * Remove overlying content.
   */
  @Override
  public T push(T object) {
    //If the stack is too big, remove elements until it's the right size.
    while (this.size()>=this.maxStackHeight) {
      this.remove(0);
    }
    return super.push(object);
  }
}
