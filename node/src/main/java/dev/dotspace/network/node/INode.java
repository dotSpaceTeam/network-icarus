package dev.dotspace.network.node;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;


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
   * Calls {@link INode#bean(Class)} with {@link Optional#orElseThrow(Supplier)}
   */
  @NotNull <T> T beanElseThrow(@Nullable final Class<T> beanClass);

  /**
   * Run spring event.
   *
   * @param object to run.
   * @return instance to chain.
   */
  @NotNull INode executeEvent(@Nullable final Object object);

  /**
   * Get id of node runtime.
   *
   * @return id of node.
   */
  @NotNull String nodeId();
}
