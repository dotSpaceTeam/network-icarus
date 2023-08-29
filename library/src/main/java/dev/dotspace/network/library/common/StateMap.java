package dev.dotspace.network.library.common;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.dotspace.common.function.ThrowableRunnable;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


@Log4j2
public final class StateMap<OBJECT> {
  /**
   * Store values of state runnable.
   */
  private final @NotNull Multimap<OBJECT, ThrowableRunnable> stateMap;

  private StateMap() {
    this.stateMap = HashMultimap.create();
  }

  /**
   * Add state to object.
   *
   * @param object   to set as enum state.
   * @param runnable to set as execute runnable.
   * @throws NullPointerException if one parameter is null.
   */
  public void append(@Nullable final OBJECT object,
                     @Nullable final ThrowableRunnable runnable) {
    //Null check
    Objects.requireNonNull(object);
    Objects.requireNonNull(runnable);

    //Add state and runnable
    this.stateMap.put(object, runnable);
  }

  /**
   * Execute runnable list for state.
   *
   * @param state to execute.
   * @throws NullPointerException if state is null.
   */
  public void executeRunnable(@Nullable final OBJECT state) {
    //Null check
    Objects.requireNonNull(state);

    this.stateMap
        //Get list of states.
        .get(state)
        //Execute them.
        .forEach(throwableRunnable -> {
          try {
            //Run runnable otherwise throw.
            throwableRunnable.run();
          } catch (final Throwable runnableError) {
            log.warn("Error while executing runnable of state={}.", state, runnableError);
          }
        });
  }

  //static

  /**
   * Create new instance.
   *
   * @param <OBJECT> generic type of map.
   * @return instance of state map.
   */
  public static <OBJECT> @NotNull StateMap<OBJECT> createMap() {
    return new StateMap<>();
  }
}
