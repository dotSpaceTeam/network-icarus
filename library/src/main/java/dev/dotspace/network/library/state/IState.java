package dev.dotspace.network.library.state;

import org.jetbrains.annotations.NotNull;

public interface IState<TYPE> {
  /**
   * State.
   *
   * @return type of TYPE.
   */
  @NotNull TYPE state();

  /**
   * Info message to current state.
   *
   * @return message.
   */
  @NotNull String message();
}
