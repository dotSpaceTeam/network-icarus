package dev.dotspace.network.library.prohibit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


//Swagger
@Schema(
    name="ProhibitReason",
    description="Reason for a prohibit."
)
public record ImmutableProhibitReason(@NotNull ProhibitType type,
                                      @NotNull String name,
                                      @NotNull String title,
                                      @NotNull String description
) implements IProhibitReason {
  /**
   * Convert {@link IProhibitReason} to {@link ImmutableProhibitReason}.
   *
   * @param reason to convert.
   * @return instance of {@link IProhibitReason}.
   */
  public static @NotNull IProhibitReason of(@Nullable final IProhibitReason reason) {
    //Null check
    Objects.requireNonNull(reason);

    return new ImmutableProhibitReason(reason.type(), reason.name(), reason.title(), reason.description());
  }
}