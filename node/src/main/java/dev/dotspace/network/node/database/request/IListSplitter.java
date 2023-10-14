package dev.dotspace.network.node.database.request;

import dev.dotspace.common.function.ThrowableFunction;
import dev.dotspace.common.function.ThrowableSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;


/**
 * Ways to format a list -> also request.
 */
public interface IListSplitter<ENTITY> {
  /**
   * Instance of pagination.
   * If sort is present -> also put in pagination.
   *
   * @return instance pageable.
   */
  @NotNull IListSplitter<ENTITY> pagination(@Nullable final ThrowableFunction<Pageable, List<ENTITY>> pageableConsumer);

  /**
   * Sort algorithm of formatter.
   *
   * @return instance of sort.
   */
  @NotNull IListSplitter<ENTITY> sort(@Nullable final ThrowableFunction<Sort, List<ENTITY>> sortConsumer);

  /**
   * Sort algorithm of formatter.
   *
   * @return instance of sort.
   */
  @NotNull IListSplitter<ENTITY> none(@Nullable final ThrowableSupplier<List<ENTITY>> noneSupplier);

  /**
   * Run algorithm.
   */
  @NotNull List<ENTITY> execute();
}
