package dev.dotspace.network.library.data;

import io.swagger.v3.oas.annotations.media.Schema;
import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * Object stored only a list.
 */
//Swagger
@Schema(implementation=ImmutableListObject.class)
public interface IListObject<TYPE> {
  /**
   * @return stored list.
   */
  //Swagger
  @Schema(example="[\"First\", \"Second\"]", description="List stored in object")
  @NotNull List<TYPE> list();
}
