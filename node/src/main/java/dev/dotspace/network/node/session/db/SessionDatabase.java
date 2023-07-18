package dev.dotspace.network.node.session.db;

import dev.dotspace.common.SpaceLibrary;
import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.network.library.session.*;
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
public final class SessionDatabase implements ISessionManipulator {

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
  public @NotNull CompletableResponse<List<ISession>> getSessionList(@Nullable String uniqueId) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);

      final ProfileEntity profile = this.profileRepository
        .findByUniqueId(uniqueId)
        .orElseThrow(() -> {
          log.error("No profile={} found to list sessions from.", uniqueId);
          return new NullPointerException();
        });

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
  public @NotNull CompletableResponse<ISession> getSession(@Nullable String uniqueId,
                                                           @Nullable Long sessionId) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(sessionId);

      return this.sessionRepository
        //Find element by id.
        .findById(sessionId)
        //Check if session was performed by uniqueId.
        .filter(sessionEntity -> sessionEntity.profile().uniqueId().equalsIgnoreCase(uniqueId))
        //Map session to ISession.
        .map(ImmutableSession::of)
        //Else error, no session or profile does not match.
        .orElseThrow(() -> {
          log.error("No session={} found for profile={}.", sessionId, uniqueId);
          return new NullPointerException();
        });
    });
  }

  @Override
  public @NotNull CompletableResponse<IPlaytime> getPlaytime(@Nullable String uniqueId) {
    return SpaceLibrary.completeResponseAsync(() -> {
        final ProfileEntity profile = this.profileRepository
          .findByUniqueId(uniqueId)
          .orElseThrow(() -> {
            log.error("No profile={} to calculate playtime from..", uniqueId);
            return new NullPointerException();
          });

        return ImmutablePlaytime.with(this.sessionRepository.calculatePlaytime(profile.id()).orElseThrow());
      });
  }

  /**
   * See {@link ISessionManipulator#createSession(String)}.
   */
  @Override
  public @NotNull CompletableResponse<ISession> createSession(@Nullable String uniqueId) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);

      final ProfileEntity profile = this.profileRepository
        .findByUniqueId(uniqueId)
        .orElseThrow(() -> {
          log.error("No profile={} found to create session.", uniqueId);
          return new NullPointerException();
        });

      return ImmutableSession.of(this.sessionRepository.save(new SessionEntity(profile, new Date(), null)));
    });
  }

  /**
   * See {@link ISessionManipulator#completeSession(String, Long)}.
   */
  @Override
  public @NotNull CompletableResponse<ISession> completeSession(@Nullable String uniqueId,
                                                                @Nullable Long sessionId) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(sessionId);

      //Get session of id.
      final SessionEntity session = this.sessionRepository
        .findById(sessionId)
        .filter(sessionEntity -> sessionEntity.profile().uniqueId().equalsIgnoreCase(uniqueId))
        .filter(sessionEntity -> !sessionEntity.closed())
        .orElseThrow(() -> {
          log.error("No session={} found to close.", sessionId);
          return new NullPointerException();
        });

      //Update end date.
      session.endDate(new Date());

      //Return modified session.s
      return ImmutableSession.of(this.sessionRepository.save(session));
    });
  }
}
