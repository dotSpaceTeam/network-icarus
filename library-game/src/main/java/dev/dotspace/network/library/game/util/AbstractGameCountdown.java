package dev.dotspace.network.library.game.util;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;


@Accessors(fluent=true)
public abstract class AbstractGameCountdown implements GameCountdown {
  /**
   * Also implemented {@link GameCountdown#startValue()}
   */
  @Getter
  private long startValue;
  /**
   * Also implemented {@link GameCountdown#value()}
   */
  @Getter
  private long value;
  /**
   * Also implemented {@link GameCountdown#done()}
   */
  @Getter
  private boolean done;

  @Getter
  @Setter
  private @Nullable Supplier<Boolean> tickCondition;

  private final @Nullable Runnable runnable;

  public AbstractGameCountdown(@Nullable final Runnable runnable) {
    this.runnable = runnable;
  }

  @Override
  public long tick() {
    //Check if done, return value.
    if (this.done) {
      return this.value;
    }

    //No tick
    if (this.tickCondition != null && !this.tickCondition.get()) {
      return this.value;
    }

    //Decrement.
    this.value--;

    //Return new value.
    return this.value;
  }

  @Override
  public long reset() {
    //Reset values.
    this.value = this.startValue;
    this.done = false;

    return this.value;
  }

  @Override
  public @NotNull GameCountdown set(long value) {
    this.startValue = value;
    //Reset countdown.
    this.reset();

    return this;
  }

  @Override
  public void onEnd() {
    //Execute runnable if present.
    if (this.runnable != null) {
      this.runnable.run();
    }
  }
}
