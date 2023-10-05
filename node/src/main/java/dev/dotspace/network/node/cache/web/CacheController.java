package dev.dotspace.network.node.cache.web;

import dev.dotspace.network.library.state.ImmutableBooleanState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Log4j2
@RequestMapping(value="/api/v1/cache")
//Swagger
@Tag(name="Cache Endpoint", description="Clear node cache.")
public final class CacheController {


  //@Autowired
  //private CachingService cachingService;
//Todo

  /**
   * Get an profile from unique id.
   */
  @Cacheable
  @GetMapping("/clear")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Clear node cache.",
      description="Clear ache and relocate memory.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="If cleared.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableBooleanState.class)
                  )
              })
      })
  public @NotNull ResponseEntity<ImmutableBooleanState> clearCache() {
    //Todo clear cache
    return ResponseEntity.ok(new ImmutableBooleanState(true, "Cleared."));
  }

}
