package dev.dotspace.network.node.profile.web;

import dev.dotspace.network.library.key.ImmutableKey;
import dev.dotspace.network.library.profile.IProfile;
import dev.dotspace.network.library.profile.attribute.IProfileAttribute;
import dev.dotspace.network.library.profile.ImmutableProfile;
import dev.dotspace.network.library.profile.attribute.ImmutableProfileAttribute;
import dev.dotspace.network.library.profile.experience.IExperience;
import dev.dotspace.network.library.profile.experience.ImmutableExperience;
import dev.dotspace.network.node.exception.ElementException;
import dev.dotspace.network.node.exception.ElementNotPresentException;
import dev.dotspace.network.node.profile.db.ProfileDatabase;
import dev.dotspace.network.node.profile.db.experience.ExperienceDatabase;
import dev.dotspace.network.node.web.AbstractRestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController
@Log4j2
@RequestMapping(value="/v1/profile")
//Swagger
@Tag(name="Profile", description="Manipulate and get player profiles.")
public final class ProfileController extends AbstractRestController {
  /**
   * Profile database.
   */
  @Autowired
  private ProfileDatabase profileDatabase;
  /**
   * Experience Database.
   */
  @Autowired
  private ExperienceDatabase experienceDatabase;

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
              description="Profile if present.",
              content={
                  @Content(
                      mediaType=MediaType.APPLICATION_JSON_VALUE,
                      schema=@Schema(implementation=ImmutableProfile.class)
                  )
              })
      })
  public ResponseEntity<IProfile> getProfile(@PathVariable @NotNull final String uniqueId) throws ElementException {
    return ResponseEntity.ok(this.profileDatabase.getProfile(uniqueId));
  }

  /**
   * Insert an new profile from unique id.
   */
  @PutMapping
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
  public ResponseEntity<IProfile> createProfile(@RequestBody @NotNull final ImmutableProfile immutableProfile) throws ElementException {
    return ResponseEntity.ok(this.profileDatabase.createProfile(immutableProfile.uniqueId(), immutableProfile.profileType()));
  }

  /**
   * Get attributes of uniqueId
   */
  @GetMapping("/{uniqueId}/attributes")
  @ResponseBody
  public ResponseEntity<List<IProfileAttribute>> getProfileAttributes(@PathVariable @NotNull final String uniqueId) throws ElementNotPresentException {
    return ResponseEntity.ok(this.profileDatabase.getAttributes(uniqueId));
  }

  /**
   * Get attributes of uniqueId
   */
  @GetMapping("/{uniqueId}/attributes/{attribute}")
  @ResponseBody
  public ResponseEntity<IProfileAttribute> getProfileAttribute(@PathVariable @NotNull final String uniqueId,
                                                               @PathVariable @NotNull final String attribute) throws ElementNotPresentException {
    return ResponseEntity.ok(this.profileDatabase.getAttribute(uniqueId, attribute));
  }


  /**
   * Get attributes of uniqueId
   */
  @PostMapping("/{uniqueId}/attributes")
  @ResponseBody
  public ResponseEntity<IProfileAttribute> postProfileAttribute(@PathVariable @NotNull final String uniqueId,
                                                                @RequestBody @NotNull final ImmutableProfileAttribute immutableProfileAttribute) throws ElementException {
    return ResponseEntity
        .ok(this.profileDatabase.setAttribute(uniqueId, immutableProfileAttribute.key(), immutableProfileAttribute.value()));
  }

  /**
   * Get attributes of uniqueId
   */
  @DeleteMapping("/{uniqueId}/attributes")
  @ResponseBody
  public ResponseEntity<IProfileAttribute> deleteProfileAttribute(@PathVariable @NotNull final String uniqueId,
                                                                  @RequestBody @NotNull final ImmutableKey immutableKey) throws ElementException {
    return ResponseEntity.ok(this.profileDatabase.setAttribute(uniqueId, immutableKey.key(), null));
  }

  //===== Experience

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
                      schema=@Schema(implementation=List.class)
                  )
              })
      })
  public ResponseEntity<List<IExperience>> addExperience(@PathVariable @NotNull final String uniqueId,
                                                         //Swagger
                                                         @Schema(type="Experience map.", implementation=Map.class)
                                                         @RequestBody @NotNull final Map<String, Long> experienceMap) {
    return ResponseEntity.ok(experienceMap
        .entrySet()
        .stream()
        .map(stringLongEntry -> {
          final String name = stringLongEntry.getKey();
          final long experience = stringLongEntry.getValue();

          log.debug("Updating experience={}(add {}) for={}.", name, experience, uniqueId);
          //Add Experience.
          try {
            //Add experience.
            return ImmutableExperience.of(this.experienceDatabase.addExperience(uniqueId, name, experience));
          } catch (final ElementException exception) {
            log.warn("Error while adding experience to "+uniqueId+".");
            return null;
          }
        })
        //Filter not present instances.
        .filter(Objects::nonNull)
        //Stream to list.
        .toList());
  }

  /**
   * Get attributes of uniqueId
   */
  @GetMapping("/{uniqueId}/experience")
  @ResponseBody
  public ResponseEntity<List<IExperience>> getExperience(@PathVariable @NotNull final String uniqueId,
                                                         @RequestParam(required=false) final String name) throws ElementException {
    System.out.println("content");
    //If name is present use only one.
    if (name != null) {
      return ResponseEntity.ok(Collections.singletonList(this.experienceDatabase.getExperience(uniqueId, name)));
    }

    //Return default list.
    return ResponseEntity.ok(this.experienceDatabase.getExperienceList(uniqueId));
  }
}