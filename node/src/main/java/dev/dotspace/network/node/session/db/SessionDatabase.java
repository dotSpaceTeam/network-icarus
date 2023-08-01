package dev.dotspace.network.node.session.db;

import dev.dotspace.common.SpaceLibrary;
import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.network.library.session.*;
import dev.dotspace.network.node.database.AbstractDatabase;
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
public final class SessionDatabase extends AbstractDatabase implements ISessionManipulator {
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

  /**
   * See {@link ISessionManipulator#getSessionList(String)}.
   */
  @Override
  public @NotNull CompletableResponse<List<ISession>> getSessionList(@Nullable String profileId) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(profileId);

      final ProfileEntity profile = this.profileRepository
        .findByUniqueId(profileId)
        .orElseThrow(this.failOptional("No profile='%s' found to list sessions from.".formatted(profileId)));

      return this.sessionRepository
        //Find all sessions entities.
        .findByProfile(profile)
        //Stream elements.
        .stream()
        //Map element to ISession.
        .map(ImmutableSession::of)
        .toList();
    });
  }

  /**
   * See {@link ISessionManipulator#getSession(String, Long)}.
   */
  @Override
  public @NotNull CompletableResponse<ISession> getSession(@Nullable String profileId,
                                                           @Nullable Long sessionId) {
    return SpaceLibrary.completeResponseAsync(() -> {
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
        .orElseThrow(this.failOptional("No session='%s' found for profile='%s'.".formatted(sessionId, profileId)));
    });
  }

  @Override
  public @NotNull CompletableResponse<IPlaytime> getPlaytime(@Nullable String profileId) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(profileId);

      final ProfileEntity profile = this.profileRepository
        .findByUniqueId(profileId)
        .orElseThrow(this.failOptional("No profile='%s' to calculate playtime from.".formatted(profileId)));

      return ImmutablePlaytime.with(this.sessionRepository.calculatePlaytime(profile.id()).orElseThrow());
    });
  }

  /**
   * See {@link ISessionManipulator#createSession(String)}.
   */
  @Override
  public @NotNull CompletableResponse<ISession> createSession(@Nullable String profileId) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(profileId);

      final ProfileEntity profile = this.profileRepository
        .findByUniqueId(profileId)
        .orElseThrow(this.failOptional("No profile='%s' found to create session.".formatted(profileId)));

      return ImmutableSession.of(this.sessionRepository.save(new SessionEntity(profile, new Date(), null)));
    });
  }

  /**
   * See {@link ISessionManipulator#completeSession(String, Long)}.
   */
  @Override
  public @NotNull CompletableResponse<ISession> completeSession(@Nullable String profileId,
                                                                @Nullable Long sessionId) {
    return SpaceLibrary.completeResponseAsync(() -> {
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
        .orElseThrow(this.failOptional("No session='%s' found to close.".formatted(sessionId)));

      //Update end date.
      session.endDate(new Date());

      //Return modified session.
      return ImmutableSession.of(this.sessionRepository.save(session));
    });
  }
}
