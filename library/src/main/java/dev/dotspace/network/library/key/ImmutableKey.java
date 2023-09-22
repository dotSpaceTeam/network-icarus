package dev.dotspace.network.library.key;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;


//Swagger
@Schema(
    name="Key",
    description="Holds key of object in field."
)
public record ImmutableKey(@NotNull String key) implements IKey {
}
