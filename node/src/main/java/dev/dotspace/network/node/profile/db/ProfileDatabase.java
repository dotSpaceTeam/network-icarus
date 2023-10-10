package dev.dotspace.network.node.profile.db;

import dev.dotspace.network.library.connection.IAddressName;
import dev.dotspace.network.library.connection.ImmutableAddressName;
import dev.dotspace.network.library.economy.IBalance;
import dev.dotspace.network.library.economy.ITransaction;
import dev.dotspace.network.library.economy.ImmutableBalance;
import dev.dotspace.network.library.economy.ImmutableCurrency;
import dev.dotspace.network.library.economy.ImmutableTransaction;
import dev.dotspace.network.library.economy.TransactionType;
import dev.dotspace.network.library.profile.IProfile;
import dev.dotspace.network.library.profile.ImmutableProfile;
import dev.dotspace.network.library.profile.ProfileType;
import dev.dotspace.network.library.profile.attribute.IProfileAttribute;
import dev.dotspace.network.library.profile.attribute.ImmutableProfileAttribute;
import dev.dotspace.network.library.profile.experience.IExperience;
import dev.dotspace.network.library.profile.experience.ImmutableExperience;
import dev.dotspace.network.library.profile.session.IPlaytime;
import dev.dotspace.network.library.profile.session.ISession;
import dev.dotspace.network.library.profile.session.ImmutablePlaytime;
import dev.dotspace.network.library.profile.session.ImmutableSession;
import dev.dotspace.network.library.time.ITimestamp;
import dev.dotspace.network.library.time.ImmutableTimestamp;
import dev.dotspace.network.node.database.AbstractDatabase;
import dev.dotspace.network.library.data.DataManipulation;
import dev.dotspace.network.node.economy.db.CurrencyEntity;
import dev.dotspace.network.node.economy.db.CurrencyRepository;
import dev.dotspace.network.node.economy.db.TransactionEntity;
import dev.dotspace.network.node.economy.db.TransactionRepository;
import dev.dotspace.network.node.database.exception.EntityNotPresentException;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Locale;
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
  /**
   * Instance to communicate tp profiles.
   */
  @Autowired
  private CurrencyRepository currencyRepository;

  /**
   * Repository to manipulates transaction.
   */
  @Autowired
  private TransactionRepository transactionRepository;

  /**
   * Update or create prof.e
   */
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

      //Fire event -> updated old profile.
      this.publishEvent(namedProfileEntity, ImmutableProfile.class, DataManipulation.UPDATE);
    }

    //Get present profile, if null create new.
    @Nullable final ProfileEntity profileEntity =
        this.profileRepository.findByUniqueId(uniqueId).orElse(null);

    if (profileEntity != null) {
      //Update name
      profileEntity.name(name);
      //Update in db.
      final IProfile updateProfile = ImmutableProfile.of(this.profileRepository.save(profileEntity));

      //Fire event -> updated name of profile.
      this.publishEvent(updateProfile, ImmutableProfile.class, DataManipulation.UPDATE);

      //end
      return updateProfile;
    }

    //Else create new one.
    final IProfile newProfile = ImmutableProfile.of(this.profileRepository.save(new ProfileEntity(uniqueId, name,
        profileType)));

    //Fire event -> Create profile.
    this.publishEvent(newProfile, ImmutableProfile.class, DataManipulation.CREATE);

    //Return new profile.
    return newProfile;
  }

  public @NotNull IProfile getProfile(@Nullable final String uniqueId) throws EntityNotPresentException {
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

  public @NotNull IProfile getProfileFromName(@Nullable final String name) throws EntityNotPresentException {
    //Null checks
    Objects.requireNonNull(name);

    return this.profileRepository
        //Get profile with name
        .findByNameIgnoreCase(name)
        //If present map to plain
        .map(ImmutableProfile::of)
        //Else error
        .orElseThrow(() -> new EntityNotPresentException("No profile for name="+name+" present."));
  }

  /**
   * If value is null -> delete.
   */
  public @NotNull IProfileAttribute setAttribute(@Nullable final String uniqueId,
                                                 @Nullable final String key,
                                                 @Nullable final String value) throws EntityNotPresentException {
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
      throw new EntityNotPresentException(null, "Attribute "+key+" not present for "+uniqueId+".");
    }

    /*
     * If attribute is already present and value to update is not null.
     */
    IProfileAttribute updatedAttribute = null;
    if (profileAttributeEntity != null && value != null) {
      profileAttributeEntity.value(value);
      //Update db
      updatedAttribute = ImmutableProfileAttribute.of(this.profileAttributeRepository.save(profileAttributeEntity));

      //Fire event -> update profile attribute.
      this.publishEvent(updatedAttribute, ImmutableProfileAttribute.class, DataManipulation.UPDATE);

      //end
      return updatedAttribute;
    }

    /*
     * If value is null delete attribute.
     */
    if (profileAttributeEntity != null) {
      this.profileAttributeRepository.deleteById(profileAttributeEntity.id());
      final IProfileAttribute deletedAttribute = ImmutableProfileAttribute.of(profileAttributeEntity);

      //Fire event -> attribute das delete.
      this.publishEvent(deletedAttribute, ImmutableProfileAttribute.class, DataManipulation.DELETE);

      return deletedAttribute;
    }

    /*
     * Otherwise if attribute is not present, insert.
     */
    final IProfileAttribute createdAttribute = ImmutableProfileAttribute
        .of(this.profileAttributeRepository.save(new ProfileAttributeEntity(profileEntity, key, value)));

    //Fire event -> created profile attribute.
    this.publishEvent(createdAttribute, ImmutableProfileAttribute.class, DataManipulation.CREATE);

    //End
    return createdAttribute;
  }

  public @NotNull IProfileAttribute removeAttribute(@Nullable final String uniqueId,
                                                    @Nullable final String key) throws EntityNotPresentException {
    //Call with null value
    return this.setAttribute(uniqueId, key, null);
  }

  public @NotNull List<IProfileAttribute> getAttributeList(@Nullable final String uniqueId) throws EntityNotPresentException {
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
                                                 @Nullable final String key) throws EntityNotPresentException {

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
        .orElseThrow(() -> new EntityNotPresentException("Not attribute="+key+" found for uniqueId="+uniqueId));
  }

  public @NotNull List<ISession> getSessionList(@Nullable final String uniqueId) throws EntityNotPresentException {
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
                                      @Nullable final Long sessionId) throws EntityNotPresentException {
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
            new EntityNotPresentException(null, "No session="+sessionId+" found for uniqueId="+uniqueId+"."));
  }

  public @NotNull ISession createSession(@Nullable final String uniqueId,
                                         @Nullable final String address) throws EntityNotPresentException {
    //Null check
    Objects.requireNonNull(uniqueId);
    Objects.requireNonNull(address);

    //Profile to create
    final ProfileEntity profile = this.profileRepository.profileElseThrow(uniqueId);

    //Create and store new session.
    final ISession createdSession = ImmutableSession.of(
        this.sessionRepository.save(new SessionEntity(profile, new Date(), null, address)));

    //Fire event -> update profile attribute.
    this.publishEvent(createdSession, ImmutableSession.class, DataManipulation.CREATE);

    //End
    return createdSession;
  }

  public @NotNull ISession completeSession(@Nullable final String uniqueId,
                                           @Nullable final Long sessionId) throws EntityNotPresentException {
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
            new EntityNotPresentException(null, "No session="+sessionId+" found to close."));

    //Update end date.
    session.endDate(new Date());

    //Return modified session.
    final ISession completedSession = ImmutableSession.of(this.sessionRepository.save(session));

    //Fire event -> update profile attribute.
    this.publishEvent(completedSession, ImmutableSession.class, DataManipulation.UPDATE);

    //End
    return completedSession;
  }

  public @NotNull ITimestamp getFirstConnect(@Nullable final String uniqueId) throws EntityNotPresentException {
    //Null check
    Objects.requireNonNull(uniqueId);

    //Get profile
    final ProfileEntity profile = this.profileRepository.profileElseThrow(uniqueId);

    //Get Address
    return this.sessionRepository
        //Get address stamp
        .firstSession(profile.id())
        //Map to plain object
        .map(ImmutableTimestamp::new)
        .orElseThrow(() -> new EntityNotPresentException("No stored first connect for uniqueId="+uniqueId+"."));
  }

  public @NotNull ITimestamp getLastConnect(@Nullable final String uniqueId) throws EntityNotPresentException {
    //Null check
    Objects.requireNonNull(uniqueId);

    //Get profile
    final ProfileEntity profile = this.profileRepository.profileElseThrow(uniqueId);

    //Get Address
    return this.sessionRepository
        //Get address stamp
        .lastSession(profile.id())
        //Map to plain object
        .map(ImmutableTimestamp::new)
        .orElseThrow(() -> new EntityNotPresentException("No stored last connect for uniqueId="+uniqueId+"."));
  }


  public @NotNull IAddressName getLatestAddress(@Nullable final String uniqueId) throws EntityNotPresentException {
    //Null check
    Objects.requireNonNull(uniqueId);

    //Get profile
    final ProfileEntity profile = this.profileRepository.profileElseThrow(uniqueId);

    //Get Address
    return this.sessionRepository
        //Get address stamp
        .latestAddress(profile.id())
        //Map to plain object
        .map(ImmutableAddressName::new)
        .orElseThrow(() -> new EntityNotPresentException("No stored ip address for uniqueId="+uniqueId+"."));
  }

  public @NotNull IPlaytime getPlaytime(@Nullable String uniqueId) throws EntityNotPresentException {
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
            EntityNotPresentException("Error while calculating play time of uniqueId="+uniqueId+".")));
  }

  public @NotNull IExperience addExperience(@Nullable final String uniqueId,
                                            @Nullable final String experienceName,
                                            long value) throws EntityNotPresentException {
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

    //Define process as create.
    DataManipulation manipulation = DataManipulation.CREATE;

    //Update if present.
    if (experience != null) {
      experience.experience(experience.experience()+value);
      //Define as updated
      manipulation = DataManipulation.UPDATE;
    } else {
      //Create new if absent.
      experience = new ExperienceEntity(profileEntity, experienceName, value);
    }

    //Return updated value.
    final IExperience updatedExperience = ImmutableExperience.of(this.experienceRepository.save(experience));

    //Fire event -> update or create experience.
    this.publishEvent(updatedExperience, ImmutableExperience.class, manipulation);

    //End
    return updatedExperience;
  }

  public @NotNull IExperience getExperience(@Nullable final String uniqueId,
                                            @Nullable final String experienceName) throws EntityNotPresentException {
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
            new EntityNotPresentException("No experience with name="+experienceName+" present for uniqueId"+uniqueId+"."));
  }

  public @NotNull List<IExperience> getExperienceList(@Nullable String uniqueId) throws EntityNotPresentException {
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

  public @NotNull IExperience getTotalExperience(@Nullable String uniqueId) throws EntityNotPresentException {
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
            () -> new EntityNotPresentException("Error while calculating total experience of uniqueId="+uniqueId+"."));
  }

  //-- Economy

  /**
   * Create new transaction.
   */
  public @NotNull ITransaction createTransaction(@Nullable final String uniqueId,
                                                 @Nullable String name,
                                                 final int amount,
                                                 @Nullable final TransactionType transactionType) throws EntityNotPresentException {
    //Null check
    Objects.requireNonNull(uniqueId);
    Objects.requireNonNull(name);
    Objects.requireNonNull(transactionType);

    //lower
    name = name.toLowerCase(Locale.ROOT);

    //Get profile of transaction
    final ProfileEntity profileEntity = this.profileRepository.profileElseThrow(uniqueId);
    //Get currency
    final CurrencyEntity currencyEntity = this.currencyRepository.nameElseThrow(name);

    //Convert amount to positive if deposited, negative if withdrawn.
    final int transactionAmount = Math.abs(amount)*(transactionType == TransactionType.WITHDRAW ? -1 : 1);
    log.debug("Converted transaction amount from={} to={}.", amount, transactionAmount);

    //Create transaction
    final ITransaction transaction = ImmutableTransaction.of(this.transactionRepository.save(
        new TransactionEntity(profileEntity, currencyEntity, transactionAmount, transactionType)));

    //Fire event -> created transaction.
    this.publishEvent(transaction, ImmutableExperience.class, DataManipulation.CREATE);

    return transaction;
  }

  public @NotNull IBalance getBalance(@Nullable final String uniqueId,
                                      @Nullable String name) throws EntityNotPresentException {
    //Null check
    Objects.requireNonNull(uniqueId);
    Objects.requireNonNull(name);

    //lower
    name = name.toLowerCase(Locale.ROOT);

    //Get profile of transaction
    final ProfileEntity profileEntity = this.profileRepository.profileElseThrow(uniqueId);
    //Get currency
    final CurrencyEntity currencyEntity = this.currencyRepository.nameElseThrow(name);

    final long balance = this.transactionRepository
        //Db calculate
        .calculateBalance(currencyEntity.id(), profileEntity.id())
        //Else zero.
        .orElse(0L);

    //Return balance.
    return new ImmutableBalance(ImmutableCurrency.of(currencyEntity), balance);
  }
}
