package dev.dotspace.network.node.economy.web;

import dev.dotspace.network.library.economy.ICurrency;
import dev.dotspace.network.library.economy.ImmutableCurrency;
import dev.dotspace.network.node.economy.db.EconomyDatabase;
import dev.dotspace.network.node.database.exception.EntityNotPresentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/economy")
//Swagger
@Tag(name="Economy Endpoint", description="Manipulate and get currencies.")
public final class EconomyController {
  /**
   * Economy database.
   */
  @Autowired
  private EconomyDatabase economyDatabase;

  /**
   * Insert an new profile from unique id.
   */
  @GetMapping("/currency")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get all stored currencies.",
      description="List all existing currencies of system.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return list of all currencies.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableCurrency.class)
                  )
              })
      })
  public ResponseEntity<List<ICurrency>> getCurrencyList() {
    return ResponseEntity.ok(this.economyDatabase.getCurrencyList());
  }

  /**
   * Post currency.
   */
  @PostMapping("/currency")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Update or create currency.",
      description="Create or update new currency -> name is persistent.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return updated or created currency",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableCurrency.class)
                  )
              })
      })
  public ResponseEntity<ICurrency> createCurrency(@RequestBody @NotNull final ImmutableCurrency immutableCurrency) {
    return ResponseEntity.ok(this.economyDatabase.createCurrency(immutableCurrency.name(),
        immutableCurrency.symbol(),
        immutableCurrency.display(),
        immutableCurrency.displayPlural()));
  }

  /**
   * Get currency
   */
  @GetMapping("/currency/{name}")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get currency of name.",
      description="Get currency associated to name, 404 if no currency present.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return present currency.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableCurrency.class)
                  )
              })
      })
  public @NotNull ResponseEntity<ICurrency> getCurrency(@PathVariable @NotNull final String name) throws EntityNotPresentException {
    return ResponseEntity.ok(this.economyDatabase.getCurrency(name));
  }
}
