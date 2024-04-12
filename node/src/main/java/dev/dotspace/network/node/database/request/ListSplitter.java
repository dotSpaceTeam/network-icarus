package dev.dotspace.network.node.database.request;

import dev.dotspace.common.function.ThrowableFunction;
import dev.dotspace.common.function.ThrowableSupplier;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Log4j2
public final class ListSplitter<ENTITY> implements IListSplitter<ENTITY> {
  /**
   * pattern for regex.
   */
  private final static Pattern PATTERN = Pattern.compile("(pagination|sort)::([a-zA-Z0-9]*)::([a-zA-Z0-9]*)&*");

  private final @NotNull String content;

  /**
   * Get pagination of formatter.
   */
  private @Nullable ThrowableFunction<Pageable, List<ENTITY>> pageableConsumer;
  private @Nullable ThrowableFunction<Sort, List<ENTITY>> sortConsumer;

  private @Nullable ThrowableSupplier<List<ENTITY>> noneSupplier;

  /**
   * Construct with pageable and sort.
   */
  public ListSplitter(@Nullable final String content) {
    //If null -> empty string.
    this.content = content == null ? "" : content;
  }

  @Override
  public @NotNull IListSplitter<ENTITY> pagination(@Nullable ThrowableFunction<Pageable, List<ENTITY>> pageableConsumer) {
    this.pageableConsumer = pageableConsumer;
    return this;
  }

  @Override
  public @NotNull IListSplitter<ENTITY> sort(@Nullable ThrowableFunction<Sort, List<ENTITY>> sortConsumer) {
    this.sortConsumer = sortConsumer;
    return this;
  }

  @Override
  public @NotNull IListSplitter<ENTITY> none(@Nullable ThrowableSupplier<List<ENTITY>> noneSupplier) {
    this.noneSupplier = noneSupplier;
    return this;
  }

  @Override
  public @NotNull List<ENTITY> execute() {
    //Parse content.
    final Matcher matcher = PATTERN.matcher(this.content);

    @Nullable Pageable pageable = null;
    @Nullable Sort sort = null;

    //Run only if content is present -> safe performance.
    if (!this.content.isEmpty()) {
      //Loop trough found results.
      while (matcher.find()) {
        //Type of element.
        final FormatterType type;

        try {
          type = FormatterType.valueOf(matcher.group(1).toUpperCase(Locale.ROOT));
          //Error if value not present
        } catch (final IllegalArgumentException exception) {
          //Got to next found element.
          continue;
        }

        //Value of element.
        final String firstValue = matcher.group(2);
        //Option, not implemented.
        final String secondValue = matcher.group(3);

        //Log info.
        log.info("Parsing filter element: {}.", type);

        if (type == FormatterType.PAGINATION) {
          final int page;
          final int pageElementCount;
          try {
            page = Integer.parseInt(firstValue);
            pageElementCount = Integer.parseInt(secondValue);
            //Set pageable instance.
            pageable = PageRequest.of(page, pageElementCount);
            log.info("Set pageable to page={} with elements={}.", page, pageElementCount);
          } catch (final Exception exception) {
            log.warn("Error while creating pagination instance.", exception);
          }
          //Next found element.
          continue;
        }

        if (type == FormatterType.SORT) {
          final SortDirection filterSort;
          try {
            filterSort = SortDirection.valueOf(firstValue.toUpperCase(Locale.ROOT));

            //Set pageable instance.
            sort = Sort.by(secondValue);

            if (filterSort == SortDirection.ASC) {
              sort.ascending();
            } else if (filterSort == SortDirection.DESC) {
              sort.descending();
            }
            log.info("Set sort to by={} with type={}.", secondValue, filterSort);
          } catch (final Exception exception) {
            log.warn("Error while creating sort instance.", exception);
          }
          //Next found element.
          continue;
        }
      }

    }

    try {
      //If pageable present -> just use page
      if (pageable != null && sort == null && this.pageableConsumer != null) {
        return this.pageableConsumer.apply(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
      }
      if (pageable != null && this.pageableConsumer != null) {
        //Combine pageable with -> combine with sort.
        return this.pageableConsumer.apply(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort));
      }
      if (sort != null && this.sortConsumer != null) {
        //Just sort.
        return this.sortConsumer.apply(sort);
      }
      //else supplier other content.
      if (this.noneSupplier != null) {
        return this.noneSupplier.get();
      }
    } catch (final Throwable throwable) {
      //Error
      log.warn("Error while supplying content to list formatter.");
    }
    //Return empty list.
    return Collections.emptyList();
  }

  //Static
  public static <ENTITY> @NotNull IListSplitter<ENTITY> parse(@Nullable final String pattern) {
    return new ListSplitter<>(pattern);
  }
}
