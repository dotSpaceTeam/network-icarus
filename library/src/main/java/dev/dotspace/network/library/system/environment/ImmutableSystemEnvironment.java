package dev.dotspace.network.library.system.environment;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;


/**
 * Immutable implementation for {@link ISystemEnvironment}.
 */
//Swagger
@Schema(
    name="SystemEnvironment",
    description="Environment parameters to run clients properly."
)
public record ImmutableSystemEnvironment(@NotNull String nodeVersion,
                                         @NotNull String rabbitMq
) implements ISystemEnvironment {
}
