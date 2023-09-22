package dev.dotspace.network.library.state;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;


//Swagger
@Schema(
    name="BooleanState",
    description="Boolean state of something."
)
public record ImmutableBooleanState(@NotNull Boolean state,
                                    @NotNull String message
) implements IState<Boolean> {
}
