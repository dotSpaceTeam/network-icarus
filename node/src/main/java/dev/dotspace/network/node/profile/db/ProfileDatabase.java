package dev.dotspace.network.node.profile.db;

import dev.dotspace.network.library.profile.IProfile;
import dev.dotspace.network.library.profile.IProfileRecord;
import dev.dotspace.network.library.profile.ImmutableProfileRecord;
import dev.dotspace.network.library.profile.attribute.IProfileAttribute;
import dev.dotspace.network.library.profile.ImmutableProfile;
import dev.dotspace.network.library.profile.attribute.ImmutableProfileAttribute;
import dev.dotspace.network.library.profile.ProfileType;
import dev.dotspace.network.node.database.AbstractDatabase;
import dev.dotspace.network.node.exception.ElementAlreadyPresentException;
import dev.dotspace.network.node.exception.ElementException;
import dev.dotspace.network.node.exception.ElementNotPresentException;
import dev.dotspace.network.node.profile.db.attribute.ProfileAttributeEntity;
import dev.dotspace.network.node.profile.db.attribute.ProfileAttributeRepository;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;


@Component("profileDatabase")
@Log4j2
public final class ProfileDatabase extends AbstractDatabase {
  /**
   * Instance to communicate tp profiles.
   */
  @Autowired
  private ProfileRepository profileRepository;

  /**
   * Instance to communicate to repository.
   */
  @Autowired
  private ProfileAttributeRepository profileAttributeRepository;


  public @NotNull IProfile createProfile(@Nullable final String uniqueId,
                                         @Nullable final ProfileType profileType) throws ElementException {
    //Null checks
    Objects.requireNonNull(uniqueId);
    Objects.requireNonNull(profileType);

    /*
     * Check if uniqueId is already existing
     */
    if (this.profileRepository.existsByUniqueId(uniqueId)) {
      throw new ElementAlreadyPresentException(null, "Profile with uniqueId="+uniqueId+", already present.");
    }

    return ImmutableProfile.of(this.profileRepository.save(new ProfileEntity(uniqueId, profileType)));
  }

  public @NotNull IProfile getProfile(@Nullable String uniqueId) throws ElementException {
    //Null checks
    Objects.requireNonNull(uniqueId);

    return ImmutableProfile.of(this.profileRepository.profileElseThrow(uniqueId));
  }

  public @NotNull IProfileAttribute setAttribute(@Nullable String uniqueId,
                                                 @Nullable String key,
                                                 @Nullable String value) throws ElementException {
    //Null checks
    Objects.requireNonNull(uniqueId);
    Objects.requireNonNull(key);

    final ProfileEntity profileEntity = this.profileRepository
        .profileElseThrow(uniqueId, "No profile with uniqueId="+uniqueId+" found to set attribute.");

    /*
     * Get attribute else null.
     */
    final ProfileAttributeEntity profileAttributeEntity = this.profileAttributeRepository
        .findByProfileAndKey(profileEntity, key)
        .orElse(null);


    if (profileAttributeEntity == null && value == null) {
      //Nothing to do here.
      throw new ElementNotPresentException(null, "Attribute "+key+" not present for "+uniqueId+".");
    }

    /*
     * If attribute is already present and value to update is not null.
     */
    if (profileAttributeEntity != null && value != null) {
      profileAttributeEntity.value(value);
      return ImmutableProfileAttribute.of(this.profileAttributeRepository.save(profileAttributeEntity));
    }

    /*
     * If value is null delete attribute.
     */
    if (profileAttributeEntity != null) {
      this.profileAttributeRepository.deleteById(profileAttributeEntity.id());
      return ImmutableProfileAttribute.of(profileAttributeEntity);
    }

    /*
     * Otherwise if attribute is not present, insert.
     */
    return ImmutableProfileAttribute
        .of(this.profileAttributeRepository.save(new ProfileAttributeEntity(profileEntity, key, value)));
  }

  public @NotNull IProfileAttribute removeAttribute(@Nullable String uniqueId,
                                                    @Nullable String key) throws ElementException {
    return this.setAttribute(uniqueId, key, null);
  }

  public @NotNull List<IProfileAttribute> getAttributes(@Nullable String uniqueId) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(uniqueId);

    /*
     * Search in database.
     */
    return this.profileRepository.findByUniqueId(uniqueId)
        /*
         * Map profile to attributes.
         */
        .map(profileEntity -> this.profileAttributeRepository.findByProfile(profileEntity))
        /*
         * Map attribute entities to list of IProfileAttribute.
         */
        .map(entities -> entities.stream().map(ImmutableProfileAttribute::of).toList())
        /*
         * Else throw error.
         */
        .orElseThrow(() -> new ElementNotPresentException(null, "Not attributes found for uniqueId="+uniqueId));
  }

  public @NotNull IProfileAttribute getAttribute(@Nullable String uniqueId,
                                                 @Nullable String key) throws ElementNotPresentException {

    //Null check
    Objects.requireNonNull(uniqueId);
    Objects.requireNonNull(key);

    /*
     * Search in database.
     */
    return this.profileRepository
        .findByUniqueId(uniqueId)
        .flatMap(profileEntity -> this.profileAttributeRepository.findByProfileAndKey(profileEntity, key))
        .map(ImmutableProfileAttribute::of)
        .orElseThrow(() -> new ElementNotPresentException(null, "Not attribute="+key+" found for uniqueId="+uniqueId));
  }

  /**
   * Get name of uniqueId.
   *
   * @param uniqueId to get name from.
   * @return name wrapped in {@link IProfileRecord}.
   * @throws ElementNotPresentException if uniqueId has no matching name.
   */
  public @NotNull IProfileRecord nameFromUniqueId(@Nullable final String uniqueId) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(uniqueId);

    //Return record.
    return new ImmutableProfileRecord(uniqueId, this.getAttribute(uniqueId, "mojang.name").value());
  }

  /**
   * Get uniqueId of name.
   *
   * @param name to get uniqueId from.
   * @return name and uniqueId.
   * @throws ElementNotPresentException if not present uniqueId was found for name.
   */
  public @NotNull IProfileRecord uniqueIdFromName(@Nullable final String name) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(name);

    final ProfileAttributeEntity attribute = this
        .profileAttributeRepository
        //Get content in field.
        .findByKeyAndValueIgnoreCase("mojang.name", name)
        //Else error.
        .orElseThrow(() -> new ElementNotPresentException(null, "No profile present for name="+name));

    //Return record.
    return new ImmutableProfileRecord(attribute.profile().uniqueId(), attribute.value());
  }
}
