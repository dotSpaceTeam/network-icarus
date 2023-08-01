package dev.dotspace.network.node;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface INode {
  /**
   * Get spring bean of class.
   *
   * @param beanClass of bean.
   * @param <T>       generic type of bean.
   * @return nullable bean in optional.
   */
  @NotNull <T> Optional<T> bean(@Nullable final Class<T> beanClass);

  /**
   * Calls {@link ISpringRunner#bean(Class)} else throw error.
   */
  @NotNull <T> T beanElseThrow(@Nullable final Class<T> beanClass);

  /**
   * Run spring event.
   *
   * @param object to run.
   * @return instance to chain.
   */
  @NotNull INode executeEvent(@Nullable final Object object);
}
