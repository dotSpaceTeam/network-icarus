package dev.dotspace.network.client.rest.web.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;


@EqualsAndHashCode
@ToString
@Getter
@Accessors(fluent=true)
public final class ImmutableRestResponse<TYPE> implements IRestResponse<TYPE> {
  private final @Nullable TYPE body;
  private final @NotNull ResponseState state;
  private final long processTime;

  public ImmutableRestResponse(@Nullable TYPE body,
                               @NotNull ResponseState state,
                               long processTime) {
    this.body = body;
    this.state = state;
    this.processTime = processTime;
  }

  /**
   * See {@link IRestResponse#body()}.
   */
  @Override
  public @NotNull TYPE body() {
    return Objects.requireNonNull(this.body);
  }
}
