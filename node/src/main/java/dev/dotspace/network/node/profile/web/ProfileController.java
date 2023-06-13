package dev.dotspace.network.node.profile.web;

import dev.dotspace.network.library.key.ImmutableKey;
import dev.dotspace.network.library.profile.*;
import dev.dotspace.network.node.profile.db.ProfileDatabase;
import dev.dotspace.network.node.web.AbstractRestController;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/profile")
public final class ProfileController extends AbstractRestController {
  /**
   * Profile database.
   */
  @Autowired
  private ProfileDatabase profileDatabase;

  /**
   * Get an profile from unique id.
   *
   * @param attributes if true return {@link dev.dotspace.network.library.profile.ICombinedProfile}.
   */
  @GetMapping("/{uniqueId}")
  @ResponseBody
  public ResponseEntity<IProfile> getProfile(@PathVariable @NotNull final String uniqueId,
                                             @RequestParam(required = false) final boolean attributes) throws InterruptedException {
    final IProfile profile = Objects
      .requireNonNull(this.profileDatabase.getProfile(uniqueId).get(), "No profile for %s".formatted(uniqueId));

    /*
     * Read attributes if requested.
     */
    if (attributes) {
      final List<IProfileAttribute> profileAttributes = this.profileDatabase.getAttributes(uniqueId).get();

      return ResponseEntity.ok(new ImmutableCombinedProfile(
        profile.uniqueId(),
        profile.profileType(),
        profileAttributes == null ? Collections.emptyList() : profileAttributes));
    }

    return ResponseEntity.ok(profile);
  }

  /**
   * Insert an new profile from unique id.
   */
  @PutMapping()
  @ResponseBody
  public ResponseEntity<IProfile> insertProfile(@RequestBody @NotNull final ImmutableProfile immutableProfile) throws InterruptedException {
    return this.validateOkResponse(

      this.profileDatabase.createProfile(
        Objects.requireNonNull(immutableProfile.uniqueId(), "UniqueId of body is null"),
        Objects.requireNonNull(immutableProfile.profileType(), "ProfileType of body is null")),

      "Profile for '%s' already present!".formatted(immutableProfile.uniqueId()));
  }

  /**
   * Get attributes of uniqueId
   */
  @GetMapping("/{uniqueId}/attributes")
  @ResponseBody
  public ResponseEntity<List<IProfileAttribute>> getProfileAttributes(@PathVariable @NotNull final String uniqueId) throws InterruptedException {
    return this.validateOkResponse(
      this.profileDatabase.getAttributes(uniqueId),
      "No attributes found for %s".formatted(uniqueId));
  }

  /**
   * Get attributes of uniqueId
   */
  @GetMapping("/{uniqueId}/attributes/{attribute}")
  @ResponseBody
  public ResponseEntity<IProfileAttribute> getProfileAttribute(@PathVariable @NotNull final String uniqueId,
                                                               @PathVariable @NotNull final String attribute) throws InterruptedException {
    return this.validateOkResponse(
      this.profileDatabase.getAttribute(uniqueId, attribute),
      "No attribute(%s) found for %s".formatted(attribute, uniqueId));
  }


  /**
   * Get attributes of uniqueId
   */
  @PostMapping("/{uniqueId}/attributes")
  @ResponseBody
  public ResponseEntity<IProfileAttribute> postProfileAttribute(@PathVariable @NotNull final String uniqueId,
                                                                @RequestBody @NotNull final ImmutableProfileAttribute immutableProfileAttribute) throws InterruptedException {
    return this.validateOkResponse(
      this.profileDatabase.setAttribute(uniqueId, immutableProfileAttribute.key(), immutableProfileAttribute.value()),
      "No attribute(%s) found for %s".formatted(immutableProfileAttribute, uniqueId));
  }

  /**
   * Get attributes of uniqueId
   */
  @DeleteMapping("/{uniqueId}/attributes")
  @ResponseBody
  public ResponseEntity<IProfileAttribute> deleteProfileAttribute(@PathVariable @NotNull final String uniqueId,
                                                                  @RequestBody @NotNull final ImmutableKey immutableKey) throws InterruptedException {
    return this.validateOkResponse(
      this.profileDatabase.setAttribute(uniqueId, immutableKey.key(), null),
      "No attribute(%s) found for %s to delete".formatted(immutableKey, uniqueId));
  }
}
