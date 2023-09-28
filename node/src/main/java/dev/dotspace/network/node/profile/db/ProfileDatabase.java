package dev.dotspace.network.node.profile.db;

import dev.dotspace.network.library.profile.IProfile;
import dev.dotspace.network.library.profile.attribute.IProfileAttribute;
import dev.dotspace.network.library.profile.ImmutableProfile;
import dev.dotspace.network.library.profile.attribute.ImmutableProfileAttribute;
import dev.dotspace.network.library.profile.ProfileType;
import dev.dotspace.network.library.profile.experience.IExperience;
import dev.dotspace.network.library.profile.experience.ImmutableExperience;
import dev.dotspace.network.library.profile.session.IPlaytime;
import dev.dotspace.network.library.profile.session.ISession;
import dev.dotspace.network.library.profile.session.ImmutablePlaytime;
import dev.dotspace.network.library.profile.session.ImmutableSession;
import dev.dotspace.network.node.database.AbstractDatabase;
import dev.dotspace.network.node.exception.ElementNotPresentException;
import dev.dotspace.network.node.profile.db.attribute.ProfileAttributeEntity;
import dev.dotspace.network.node.profile.db.attribute.ProfileAttributeRepository;
import dev.dotspace.network.node.profile.db.experience.ExperienceEntity;
import dev.dotspace.network.node.profile.db.experience.ExperienceRepository;
import dev.dotspace.network.node.profile.db.experience.LevelFunction;
import dev.dotspace.network.node.profile.db.session.SessionEntity;
import dev.dotspace.network.node.profile.db.session.SessionRepository;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Component
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
  /**
   * Repository to modify sessions.
   */
  @Autowired
  private SessionRepository sessionRepository;
  /**
   * Instance to communicate to experience.
   */
  @Autowired
  private ExperienceRepository experienceRepository;


  public @NotNull IProfile updateProfile(@Nullable final String uniqueId,
                                         @Nullable final String name,
                                         @Nullable final ProfileType profileType) {
    //Null checks
    Objects.requireNonNull(uniqueId);
    Objects.requireNonNull(name);
    Objects.requireNonNull(profileType);

    //Check if profile with name and other uniqueId present
    @Nullable final ProfileEntity namedProfileEntity =
        this.profileRepository.findByNameIgnoreCase(name).orElse(null);

    //Update if namedProfileEntity is present. And id is odd.
    if (namedProfileEntity != null && !namedProfileEntity.uniqueId().equals(uniqueId)) {
      //Set pseudo name.
      namedProfileEntity.name("~"+namedProfileEntity.name()+"~"+namedProfileEntity.uniqueId());
      //Update pseudo profile
      this.profileRepository.save(namedProfileEntity);
    }

    //Get present profile, if null create new.
    @Nullable final ProfileEntity profileEntity =
        this.profileRepository.findByUniqueId(uniqueId).orElse(null);

    if (profileEntity != null) {
      //Update name
      profileEntity.name(name);
      //Update in db.
      return ImmutableProfile.of(this.profileRepository.save(profileEntity));
    }

    //Else create new one.
    return ImmutableProfile.of(this.profileRepository.save(new ProfileEntity(uniqueId, name, profileType)));
  }

  public @NotNull IProfile getProfile(@Nullable final String uniqueId) throws ElementNotPresentException {
    //Null checks
    Objects.requireNonNull(uniqueId);

    return ImmutableProfile.of(this.profileRepository.profileElseThrow(uniqueId));
  }

  public @NotNull List<IProfile> getProfileList() {
    return this.profileRepository
        //Get all profiles.
        .findAll()
        .stream()
        //Create new instances.
        .map(ImmutableProfile::of)
        .toList();
  }

  public @NotNull List<IProfile> getProfileList(@Nullable final Pageable pageable) {
    //Null check
    Objects.requireNonNull(pageable);

    return this.profileRepository
        //Get all from pageable.
        .findAll(pageable)
        .stream()
        //Create new instances.
        .map(ImmutableProfile::of)
        .toList();
  }

  public @NotNull List<IProfile> getProfileList(@Nullable final Sort sort) {
    //Null check
    Objects.requireNonNull(sort);

    return this.profileRepository
        //Get all from pageable.
        .findAll(sort)
        .stream()
        //Create new instances.
        .map(ImmutableProfile::of)
        .toList();
  }

  public @NotNull IProfile getProfileFromName(@Nullable final String name) throws ElementNotPresentException {
    //Null checks
    Objects.requireNonNull(name);

    return this.profileRepository
        //Get profile with name
        .findByNameIgnoreCase(name)
        //If present map to plain
        .map(ImmutableProfile::of)
        //Else error
        .orElseThrow(() -> new ElementNotPresentException("No profile for name="+name+" present."));
  }

  /**
   * If value is null -> delete.
   */
  public @NotNull IProfileAttribute setAttribute(@Nullable final String uniqueId,
                                                 @Nullable final String key,
                                                 @Nullable final String value) throws ElementNotPresentException {
    //Null checks
    Objects.requireNonNull(uniqueId);
    Objects.requireNonNull(key);

    //Get profile.
    final ProfileEntity profileEntity = this.profileRepository.profileElseThrow(uniqueId);

    /*
     * Get attribute else null.
     */
    final ProfileAttributeEntity profileAttributeEntity = this.profileAttributeRepository
        .findByProfileAndKey(profileEntity, key)
        .orElse(null);

//Check if nothing to update.
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

  public @NotNull IProfileAttribute removeAttribute(@Nullable final String uniqueId,
                                                    @Nullable final String key) throws ElementNotPresentException {
    return this.setAttribute(uniqueId, key, null);
  }

  public @NotNull List<IProfileAttribute> getAttributeList(@Nullable final String uniqueId) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(uniqueId);

    /*
     * Search in database.
     */
    return
        this.profileAttributeRepository
            //Throw error if profile is not present.
            .findByProfile(this.profileRepository.profileElseThrow(uniqueId))
            .stream()
            //Map attributes to plain objects.
            .map(ImmutableProfileAttribute::of).toList();
  }

  public @NotNull IProfileAttribute getAttribute(@Nullable final String uniqueId,
                                                 @Nullable final String key) throws ElementNotPresentException {

    //Null check
    Objects.requireNonNull(uniqueId);
    Objects.requireNonNull(key);

    /*
     * Search in database.
     */
    return this.profileAttributeRepository
        //Get attribute throw error if profile is not present.
        .findByProfileAndKey(this.profileRepository.profileElseThrow(uniqueId), key)
        //Map attribute to plain.
        .map(ImmutableProfileAttribute::of)
        //Else throw error.
        .orElseThrow(() -> new ElementNotPresentException("Not attribute="+key+" found for uniqueId="+uniqueId));
  }

  public @NotNull List<ISession> getSessionList(@Nullable final String uniqueId) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(uniqueId);

    //Get profile
    final ProfileEntity profile = this.profileRepository.profileElseThrow(uniqueId);

    return this.sessionRepository
        //Find all sessions entities.
        .findByProfile(profile)
        //Stream elements.
        .stream()
        //Map element to ISession.
        .map(ImmutableSession::of)
        .toList();
  }

  public @NotNull ISession getSession(@Nullable final String uniqueId,
                                      @Nullable final Long sessionId) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(uniqueId);
    Objects.requireNonNull(sessionId);

    return this.sessionRepository
        //Find element by id.
        .findById(sessionId)
        //Check if session was performed by uniqueId.
        .filter(sessionEntity -> sessionEntity.profile().uniqueId().equals(uniqueId))
        //Map session to ISession.
        .map(ImmutableSession::of)
        //Else error, no session or profile does not match.
        .orElseThrow(() ->
            new ElementNotPresentException(null, "No session="+sessionId+" found for uniqueId="+uniqueId+"."));
  }

  public @NotNull ISession createSession(@Nullable final String uniqueId,
                                         @Nullable final String address) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(uniqueId);
    Objects.requireNonNull(address);

    //Profile to create
    final ProfileEntity profile = this.profileRepository.profileElseThrow(uniqueId);

    //Create and store new session.
    return ImmutableSession.of(
        this.sessionRepository.save(new SessionEntity(profile, new Date(), null, address)));
  }

  public @NotNull ISession completeSession(@Nullable final String uniqueId,
                                           @Nullable final Long sessionId) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(uniqueId);
    Objects.requireNonNull(sessionId);

    //Get session of id.
    final SessionEntity session = this.sessionRepository
        .findById(sessionId)
        //Check if unique id equal to given uniqueId.
        .filter(sessionEntity -> sessionEntity.profile().uniqueId().equals(uniqueId))
        //Check if session is not closed.
        .filter(sessionEntity -> !sessionEntity.closed())
        .orElseThrow(() ->
            new ElementNotPresentException(null, "No session="+sessionId+" found to close."));

    //Update end date.
    session.endDate(new Date());

    //Return modified session.
    return ImmutableSession.of(this.sessionRepository.save(session));
  }


  public @NotNull IPlaytime getPlaytime(@Nullable String uniqueId) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(uniqueId);

    //Get profile
    final ProfileEntity profile = this.profileRepository.profileElseThrow(uniqueId);

    //Calculate time
    return ImmutablePlaytime.with(this.sessionRepository
        //Get time
        .calculatePlaytime(profile.id())
        //Else error.
        .orElseThrow(() -> new
            ElementNotPresentException("Error while calculating play time of uniqueId="+uniqueId+".")));
  }

  public @NotNull IExperience addExperience(@Nullable final String uniqueId,
                                            @Nullable final String experienceName,
                                            long value) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(uniqueId);
    Objects.requireNonNull(experienceName);

    //Profile
    final ProfileEntity profileEntity = this.profileRepository.profileElseThrow(uniqueId);

    //Get Experience entity.
    @Nullable ExperienceEntity experience = this.experienceRepository
        //Get entity by profile key and name
        .findByProfileAndName(profileEntity, experienceName)
        //Else return null.
        .orElse(null);

    //Update if present.
    if (experience != null) {
      experience.experience(experience.experience()+value);
    } else {
      //Create new if absent.
      experience = new ExperienceEntity(profileEntity, experienceName, value);
    }

    //Return updated value.
    return ImmutableExperience.of(this.experienceRepository.save(experience));
  }

  public @NotNull IExperience getExperience(@Nullable final String uniqueId,
                                            @Nullable final String experienceName) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(uniqueId);
    Objects.requireNonNull(experienceName);

    //Profile
    final ProfileEntity profileEntity = this.profileRepository.profileElseThrow(uniqueId);

    return this.experienceRepository
        .findByProfileAndName(profileEntity, experienceName)
        //Map to plain
        .map(ImmutableExperience::of)
        .orElseThrow(() ->
            new ElementNotPresentException("No experience with name="+experienceName+" present for uniqueId"+uniqueId+"."));
  }

  public @NotNull List<IExperience> getExperienceList(@Nullable String uniqueId) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(uniqueId);

    //Profile.
    final ProfileEntity profileEntity = this.profileRepository.profileElseThrow(uniqueId);

    return this.experienceRepository
        //List of all experience.
        .findByProfile(profileEntity)
        .stream()
        //Map to plain.
        .map(ImmutableExperience::of)
        .toList();
  }

  public @NotNull IExperience getTotalExperience(@Nullable String uniqueId) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(uniqueId);

    //Get profile.
    final ProfileEntity profileEntity = this.profileRepository.profileElseThrow(uniqueId);

    return this.experienceRepository
        //Get total amount
        .getTotal(profileEntity.id())
        //Create immutable instance.
        .map(total -> ImmutableExperience.of("Total", total, LevelFunction.function()))
        .orElseThrow(
            () -> new ElementNotPresentException("Error while calculating total experience of uniqueId="+uniqueId+"."));
  }
}
