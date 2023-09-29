package dev.dotspace.network.node.profile.web;

import dev.dotspace.network.library.connection.IAddressName;
import dev.dotspace.network.library.connection.ImmutableAddressName;
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
import dev.dotspace.network.node.exception.ElementException;
import dev.dotspace.network.node.exception.ElementNotPresentException;
import dev.dotspace.network.node.profile.db.ProfileDatabase;
import dev.dotspace.network.node.web.AbstractRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;
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
                                                      @RequestParam(required=false, defaultValue="false") final boolean nameSearch) throws ElementNotPresentException {
    //If true search profile with name
    if (nameSearch) {
      return ResponseEntity.ok(this.profileDatabase.getProfileFromName(uniqueId));
    }

    return ResponseEntity.ok(this.profileDatabase.getProfile(uniqueId));
  }

  /**
   * Insert an new profile from unique id.
   */
  @PostMapping("/")
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
   * Insert an new profile from unique id.
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
  public @NotNull ResponseEntity<List<IProfile>> getProfileList(@RequestParam(required=false) @Nullable final Integer page,
                                                                @RequestParam(required=false) @Nullable final Integer pageContent,
                                                                @RequestParam(required=false) @Nullable final String sortBy,
                                                                @RequestParam(required=false, defaultValue="false") final boolean ascending) {
    //Use pageable.
    if (page != null && pageContent != null && sortBy != null) {
      //Return request.
      return ResponseEntity.ok(this.profileDatabase
          .getProfileList(PageRequest.of(page, pageContent, this.sortBy(sortBy, ascending))));
    }

    if (sortBy != null) {
      //Return request.
      return ResponseEntity.ok(this.profileDatabase
          .getProfileList(this.sortBy(sortBy, ascending)));
    }

    //Plain get all.
    return ResponseEntity.ok(this.profileDatabase
        .getProfileList());
  }

  private @NotNull Sort sortBy(@NotNull final String sortBy,
                               final boolean ascending) {
    final Sort sort = Sort.by(sortBy);

    //Check if sorting is ascending or descending.
    if (ascending) {
      sort.ascending();
    } else {
      sort.descending();
    }

    return sort;
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
  public @NotNull ResponseEntity<List<IProfileAttribute>> getAttributes(@PathVariable @NotNull final String uniqueId) throws ElementNotPresentException {
    return ResponseEntity.ok(this.profileDatabase.getAttributeList(uniqueId));
  }

  /**
   * Get attributes of uniqueId
   */
  @PostMapping("/{uniqueId}/attribute/")
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
                                                                 @RequestBody @NotNull final ImmutableProfileAttribute immutableProfileAttribute) throws ElementNotPresentException {
    return ResponseEntity
        .ok(this.profileDatabase.setAttribute(uniqueId, immutableProfileAttribute.key(), immutableProfileAttribute.value()));
  }


  /**
   * Get attributes of uniqueId
   */
  @DeleteMapping("/{uniqueId}/attribute/")
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
                                                                    @RequestBody @NotNull final ImmutableKey immutableKey) throws ElementNotPresentException {
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
                                                                 @PathVariable @NotNull final String attribute) throws ElementNotPresentException {
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
  public @NotNull ResponseEntity<List<ISession>> getSessionList(@PathVariable @NotNull final String uniqueId) throws ElementNotPresentException {
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
                                                      @PathVariable @NotNull final Long sessionId) throws ElementNotPresentException {
    return ResponseEntity.ok(this.profileDatabase.getSession(uniqueId, sessionId));
  }


  /**
   * Create a session for uniqueId,
   */
  @PostMapping("/{uniqueId}/session/")
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
                                                         @RequestBody @NotNull final ImmutableAddressName addressName) throws ElementNotPresentException {
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
                                                        @PathVariable @NotNull final Long sessionId) throws ElementNotPresentException {
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
  public @NotNull ResponseEntity<IAddressName> getAddress(@PathVariable @NotNull final String uniqueId) throws ElementNotPresentException {
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
  public @NotNull ResponseEntity<IPlaytime> getPlaytime(@PathVariable @NotNull final String uniqueId) throws ElementNotPresentException {
    return ResponseEntity.ok(this.profileDatabase.getPlaytime(uniqueId));
  }

  /*
   * =================== Experience
   */

  /**
   * Add Experience of uniqueId
   */
  @PostMapping("/{uniqueId}/experience/")
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
                                                            @RequestBody @NotNull final ImmutableExperience experience) throws ElementNotPresentException {
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
  public @NotNull ResponseEntity<IExperience> getExperience(@PathVariable @NotNull final String uniqueId) throws ElementNotPresentException {
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
                                                            @PathVariable @NotNull final String experience) throws ElementNotPresentException {
    return ResponseEntity.ok(this.profileDatabase.getExperience(uniqueId, experience));
  }
}
