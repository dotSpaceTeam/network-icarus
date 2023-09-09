package dev.dotspace.network.node.session.db;

import dev.dotspace.network.library.session.IPlaytime;
import dev.dotspace.network.library.session.ISession;
import dev.dotspace.network.library.session.ImmutablePlaytime;
import dev.dotspace.network.library.session.ImmutableSession;
import dev.dotspace.network.node.database.AbstractDatabase;
import dev.dotspace.network.node.exception.ElementException;
import dev.dotspace.network.node.exception.ElementNotPresentException;
import dev.dotspace.network.node.profile.db.ProfileEntity;
import dev.dotspace.network.node.profile.db.ProfileRepository;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * Manipulate sessions.
 */
@Component("sessionDatabase")
@Log4j2
public final class SessionDatabase extends AbstractDatabase {
  /**
   * Repository to modify sessions.
   */
  @Autowired
  private SessionRepository sessionRepository;
  /**
   * Repository to modify profiles.
   */
  @Autowired
  private ProfileRepository profileRepository;

  public @NotNull List<ISession> getSessionList(@Nullable String profileId) throws ElementException {
    //Null check
    Objects.requireNonNull(profileId);

    final ProfileEntity profile = this.profileRepository.profileElseThrow(profileId);

    return this.sessionRepository
        //Find all sessions entities.
        .findByProfile(profile)
        //Stream elements.
        .stream()
        //Map element to ISession.
        .map(ImmutableSession::of)
        .toList();
  }

  public @NotNull ISession getSession(@Nullable String profileId,
                                      @Nullable Long sessionId) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(profileId);
    Objects.requireNonNull(sessionId);

    return this.sessionRepository
        //Find element by id.
        .findById(sessionId)
        //Check if session was performed by uniqueId.
        .filter(sessionEntity -> sessionEntity.profile().uniqueId().equalsIgnoreCase(profileId))
        //Map session to ISession.
        .map(ImmutableSession::of)
        //Else error, no session or profile does not match.
        .orElseThrow(() ->
            new ElementNotPresentException(null, "No session="+sessionId+" found for profile="+profileId+"."));
  }

  public @NotNull IPlaytime getPlaytime(@Nullable String profileId) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(profileId);

    final ProfileEntity profile = this.profileRepository
        .findByUniqueId(profileId)
        .orElseThrow(() ->
            new ElementNotPresentException(null, "No profile="+profileId+" to calculate playtime from."));

    return ImmutablePlaytime.with(this.sessionRepository.calculatePlaytime(profile.id()).orElseThrow());
  }

  public @NotNull ISession createSession(@Nullable String profileId) throws ElementException {
    //Null check
    Objects.requireNonNull(profileId);

    final ProfileEntity profile = this.profileRepository.profileElseThrow(profileId);

    return ImmutableSession.of(this.sessionRepository.save(new SessionEntity(profile, new Date(), null)));
  }

  public @NotNull ISession completeSession(@Nullable String profileId,
                                           @Nullable Long sessionId) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(profileId);
    Objects.requireNonNull(sessionId);

    //Get session of id.
    final SessionEntity session = this.sessionRepository
        .findById(sessionId)
        //Check if unique id equal to given uniqueId.
        .filter(sessionEntity -> sessionEntity.profile().uniqueId().equalsIgnoreCase(profileId))
        //Check if session is not closed.
        .filter(sessionEntity -> !sessionEntity.closed())
        .orElseThrow(() ->
            new ElementNotPresentException(null, "No session="+sessionId+" found to close."));

    //Update end date.
    session.endDate(new Date());

    //Return modified session.
    return ImmutableSession.of(this.sessionRepository.save(session));
  }
}
