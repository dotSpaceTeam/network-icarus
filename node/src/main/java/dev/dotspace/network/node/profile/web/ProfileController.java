package dev.dotspace.network.node.profile.web;

import dev.dotspace.network.library.connection.IAddressName;
import dev.dotspace.network.library.connection.ImmutableAddressName;
import dev.dotspace.network.library.economy.IBalance;
import dev.dotspace.network.library.economy.ITransaction;
import dev.dotspace.network.library.economy.ImmutableBalance;
import dev.dotspace.network.library.economy.ImmutableTransaction;
import dev.dotspace.network.library.key.ImmutableKey;
import dev.dotspace.network.library.profile.IProfile;
import dev.dotspace.network.library.profile.ImmutableProfile;
import dev.dotspace.network.library.profile.attribute.IProfileAttribute;
import dev.dotspace.network.library.profile.attribute.ImmutableProfileAttribute;
import dev.dotspace.network.library.profile.experience.IExperience;
import dev.dotspace.network.library.profile.experience.ImmutableExperience;
import dev.dotspace.network.library.profile.session.IPlaytime;
import dev.dotspace.network.library.profile.session.ISession;
import dev.dotspace.network.library.profile.session.ImmutablePlaytime;
import dev.dotspace.network.library.profile.session.ImmutableSession;
import dev.dotspace.network.node.database.exception.EntityNotPresentException;
import dev.dotspace.network.node.database.exception.IllegalEntityException;
import dev.dotspace.network.node.database.request.ListSplitter;
import dev.dotspace.network.node.profile.db.ProfileDatabase;
import dev.dotspace.network.node.web.controller.AbstractRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;


@RestController
@Log4j2
@RequestMapping(value="/api/v1/profile")
//Swagger
@Tag(name="Profile Endpoint", description="Manipulate and get player profiles.")
public final class ProfileController extends AbstractRestController {
  /**
   * Profile database.
   */
  @Autowired
  private ProfileDatabase profileDatabase;

  /**
   * Get an profile from unique id.
   */
  @GetMapping("/{uniqueId}")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get Profile information of uniqueId.",
      description="Return Profile associated to uniqueId.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Present Profile otherwise 404.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableProfile.class)
                  )
              })
      })
  public @NotNull ResponseEntity<IProfile> getProfile(@PathVariable @NotNull final String uniqueId,
                                                      @RequestParam(required=false, defaultValue="false") final boolean nameSearch)
      throws EntityNotPresentException {
    //If true search profile with name
    if (nameSearch) {
      return ResponseEntity.ok(this.profileDatabase.getProfileFromName(uniqueId));
    }

    return ResponseEntity.ok(this.profileDatabase.getProfile(uniqueId));
  }

  /**
   * Insert an new profile from unique id.
   */
  @PostMapping
  @ResponseBody
  //Swagger
  @Operation(
      summary="Create new profile for uniqueId.",
      description="Create new profile. Send endpoint profile.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Profile was created and stored successfully.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableProfile.class)
                  )
              })
      })
  public @NotNull ResponseEntity<IProfile> updateProfile(@RequestBody @NotNull final ImmutableProfile immutableProfile) {
    return ResponseEntity.ok(this.profileDatabase
        .updateProfile(immutableProfile.uniqueId(), immutableProfile.name(), immutableProfile.profileType()));
  }


  /**
   * Get multiple profiles.
   */
  @GetMapping
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get all profiles, use this function with caution.",
      description="Get all stored profiles, high data density",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Profiles as list.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableProfile.class)
                  )
              })
      })
  public @NotNull ResponseEntity<List<IProfile>> getProfileList(
      @RequestParam(required=false) @Nullable final String listPattern) {
    return ResponseEntity.ok(new ListSplitter<IProfile>(listPattern)
        //Handle if patern is pagination with or without sort
        .pagination(this.profileDatabase::getProfileList)
        //If list should be sorted.
        .sort(this.profileDatabase::getProfileList)
        //If no pattern is defined.
        .none(this.profileDatabase::getProfileList)
        //Run search.
        .execute());
  }


  /*
   * =================== Attributes
   */

  /**
   * Get attributes of uniqueId
   */
  @GetMapping("/{uniqueId}/attribute")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get all present profile attributes.",
      description="Method returns a list of all present attributes associated to profile.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="List of all attributes.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableProfileAttribute.class)
                  )
              })
      })
  public @NotNull ResponseEntity<List<IProfileAttribute>> getAttributes(@PathVariable @NotNull final String uniqueId)
      throws EntityNotPresentException {
    return ResponseEntity.ok(this.profileDatabase.getAttributeList(uniqueId));
  }

  /**
   * Get attributes of uniqueId
   */
  @PostMapping("/{uniqueId}/attribute")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Set new value as profile attribute.",
      description="Update or define new attribute.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Updated or create attribute.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableProfileAttribute.class)
                  )
              })
      })
  public @NotNull ResponseEntity<IProfileAttribute> setAttribute(@PathVariable @NotNull final String uniqueId,
                                                                 @RequestBody @NotNull final ImmutableProfileAttribute immutableProfileAttribute)
      throws EntityNotPresentException {
    return ResponseEntity
        .ok(this.profileDatabase.setAttribute(uniqueId, immutableProfileAttribute.key(), immutableProfileAttribute.value()));
  }


  /**
   * Get attributes of uniqueId
   */
  @DeleteMapping("/{uniqueId}/attribute")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Delete a profile attribute.",
      description="Delete present profile attribute.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Deleted profile attribute.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableProfileAttribute.class)
                  )
              })
      })
  public @NotNull ResponseEntity<IProfileAttribute> deleteAttribute(@PathVariable @NotNull final String uniqueId,
                                                                    @RequestBody @NotNull final ImmutableKey immutableKey)
      throws EntityNotPresentException {
    return ResponseEntity.ok(this.profileDatabase.removeAttribute(uniqueId, immutableKey.key()));
  }

  /**
   * Get attributes of uniqueId
   */
  @GetMapping("/{uniqueId}/attribute/{attribute}")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get a specific profile attribute.",
      description="Get a specific profile attribute from attribute name.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Present attribute with name.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableProfileAttribute.class)
                  )
              })
      })
  public @NotNull ResponseEntity<IProfileAttribute> getAttribute(@PathVariable @NotNull final String uniqueId,
                                                                 @PathVariable @NotNull final String attribute)
      throws EntityNotPresentException {
    return ResponseEntity.ok(this.profileDatabase.getAttribute(uniqueId, attribute));
  }

  /*
   * =================== Session
   */

  /**
   * Get all sessions for uniqueId,
   */
  @GetMapping("/{uniqueId}/session")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get all stored sessions of profile, use this function with caution.",
      description="Request every data for session stored, high data density.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Returns a list of every session.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableSession.class)
                  )
              })
      })
  public @NotNull ResponseEntity<List<ISession>> getSessionList(@PathVariable @NotNull final String uniqueId)
      throws EntityNotPresentException {
    return ResponseEntity.ok(this.profileDatabase.getSessionList(uniqueId));
  }

  /**
   * Get a session from uniqueId.
   */
  @GetMapping("/{uniqueId}/session/{sessionId}")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get a specific stored session of client.",
      description="Request data for session stored.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return session with matching sessionId.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableSession.class)
                  )
              })
      })
  public @NotNull ResponseEntity<ISession> getSession(@PathVariable @NotNull final String uniqueId,
                                                      @PathVariable @NotNull final Long sessionId)
      throws EntityNotPresentException {
    return ResponseEntity.ok(this.profileDatabase.getSession(uniqueId, sessionId));
  }


  /**
   * Create a session for uniqueId,
   */
  @PostMapping("/{uniqueId}/session")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Create a new session for client.",
      description="Create a new session with no end timestamp.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return session created.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableSession.class)
                  )
              })
      })
  public @NotNull ResponseEntity<ISession> createSession(@PathVariable @NotNull final String uniqueId,
                                                         @RequestBody @NotNull final ImmutableAddressName addressName)
      throws EntityNotPresentException {
    return ResponseEntity.ok(this.profileDatabase.createSession(uniqueId, addressName.address()));
  }

  /**
   * Create a session for uniqueId,
   */
  @PutMapping("/{uniqueId}/session/{sessionId}")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Close a session for client with sessionId.",
      description="Set current timestamp as end for session.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return session closed.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableSession.class)
                  )
              })
      })
  public @NotNull ResponseEntity<ISession> closeSession(@PathVariable @NotNull final String uniqueId,
                                                        @PathVariable @NotNull final Long sessionId)
      throws EntityNotPresentException {
    return ResponseEntity.ok(this.profileDatabase.completeSession(uniqueId, sessionId));
  }

  /**
   * Get playtime of uniqueId
   */
  @GetMapping("/{uniqueId}/ip")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get last stored ip address of player.",
      description="Get last ip address of latest player session.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return present address or 404 if empty.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableAddressName.class)
                  )
              })
      })
  public @NotNull ResponseEntity<IAddressName> getAddress(@PathVariable @NotNull final String uniqueId)
      throws EntityNotPresentException {
    return ResponseEntity.ok(this.profileDatabase.getLatestAddress(uniqueId));
  }

  /*
   * =================== Playtime
   */

  /**
   * Get playtime of uniqueId
   */
  @GetMapping("/{uniqueId}/playtime")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get total time a client played on server in milliseconds.",
      description="Returns the calculated time of player been connected to network.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Final playtime is calculated from all session durations. Session need to be closed to "+
                  "count to playtime.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutablePlaytime.class)
                  )
              })
      })
  public @NotNull ResponseEntity<IPlaytime> getPlaytime(@PathVariable @NotNull final String uniqueId)
      throws EntityNotPresentException {
    return ResponseEntity.ok(this.profileDatabase.getPlaytime(uniqueId));
  }

  /*
   * =================== Experience
   */

  /**
   * Add Experience of uniqueId
   */
  @PostMapping("/{uniqueId}/experience")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Add experience to profile.",
      description="Add experience points to profile. With this method multiple experience points can be added.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Experience was added successfully.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableExperience.class)
                  )
              })
      })
  public @NotNull ResponseEntity<IExperience> addExperience(@PathVariable @NotNull final String uniqueId,
                                                            @RequestBody @NotNull final ImmutableExperience experience)
      throws EntityNotPresentException {
    //Null check
    Objects.requireNonNull(experience);

    return ResponseEntity.ok(this.profileDatabase.addExperience(uniqueId, experience.name(), experience.experience()));
  }

  /**
   * Get total experience of uniqueId
   */
  @GetMapping("/{uniqueId}/experience")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get total experience to profile.",
      description="Total amount of experience a profile has.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Experience as total calculated.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableExperience.class)
                  )
              })
      })
  public @NotNull ResponseEntity<IExperience> getExperience(@PathVariable @NotNull final String uniqueId)
      throws EntityNotPresentException {
    return ResponseEntity.ok(this.profileDatabase.getTotalExperience(uniqueId));
  }

  /**
   * Get specific experience of uniqueId
   */
  @GetMapping("/{uniqueId}/experience/{experience}")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get a specific stored experience of client.",
      description="Request amount of experience stored under name.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return experience with matching name.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableExperience.class)
                  )
              })
      })
  public @NotNull ResponseEntity<IExperience> getExperience(@PathVariable @NotNull final String uniqueId,
                                                            @PathVariable @NotNull final String experience)
      throws EntityNotPresentException {
    return ResponseEntity.ok(this.profileDatabase.getExperience(uniqueId, experience));
  }


  /*
   * =================== Economy
   */

  /**
   * Add transaction
   */
  @PostMapping("/{uniqueId}/economy")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Create new transaction for player.",
      description="Create a new transaction for player, define type. (Withdraw or Deposit)",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return created transaction.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableTransaction.class)
                  )
              })
      })
  public @NotNull ResponseEntity<ITransaction> createEconomyTransaction(
      @PathVariable @NotNull final String uniqueId,
      @RequestBody @NotNull final ImmutableTransaction immutableTransaction,
      @RequestParam(required=false, defaultValue="false") final boolean negativeBalance)
      throws EntityNotPresentException, IllegalEntityException {
    //Check if player has enough coins.
    if (!negativeBalance) {
      final long balance = this.profileDatabase
          //Get current balance
          .getBalance(uniqueId, immutableTransaction.currency())
          //Get balance value
          .balance();

      //Throw error if transaction will be negative.
      if (balance-immutableTransaction.amount()<0) {
        //Error
        throw new IllegalEntityException("There is not enough available in the account.");
      }
    }

    //Response
    return ResponseEntity.ok(this.profileDatabase.createTransaction(uniqueId,
        immutableTransaction.currency(),
        immutableTransaction.amount(),
        immutableTransaction.transactionType()));
  }

  /**
   * Get balance for currency
   */
  @GetMapping("/{uniqueId}/economy/{currency}")
  @ResponseBody
  //Swagger
  @Operation(
      summary="Get balance for player and currency.",
      description="Get balance of currency for player.",
      responses={
          @ApiResponse(
              responseCode="200",
              description="Return balance.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableBalance.class)
                  )
              })
      })
  public @NotNull ResponseEntity<IBalance> createEconomyTransaction(@PathVariable @NotNull final String uniqueId,
                                                                    @PathVariable @NotNull final String currency)
      throws EntityNotPresentException {
    return ResponseEntity.ok(this.profileDatabase.getBalance(uniqueId, currency));
  }
}
