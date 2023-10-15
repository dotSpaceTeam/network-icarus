package dev.dotspace.network.library.data;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

import java.util.List;


//Swagger
@Schema(
    name="ListObject",
    description="Basic wrap to store a list in an object."
)
public record ImmutableListObject<TYPE>(@NotNull List<TYPE> list) implements IListObject<TYPE> {
}
