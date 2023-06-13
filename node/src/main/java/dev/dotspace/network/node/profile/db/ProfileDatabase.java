package dev.dotspace.network.node.profile.db;

import dev.dotspace.common.SpaceLibrary;
import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.network.library.profile.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component("profileDatabase")
public final class ProfileDatabase implements IProfileManipulator {
  /**
   * Logger
   */
  private final static @NotNull Logger LOGGER = LogManager.getLogger(ProfileDatabase.class);

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
   * See {@link IProfileManipulator#createProfile(String, ProfileType)}.
   */
  @Override
  public @NotNull CompletableResponse<IProfile> createProfile(@Nullable String uniqueId,
                                                              @Nullable ProfileType profileType) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null checks
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(profileType);

      /*
       * Check if uniqueId is already existing
       */
      if (this.profileRepository.existsByUniqueId(uniqueId)) {
        LOGGER.info("Profile with uniqueId='{}', already present.", uniqueId);
        return null;
      }

      return ImmutableProfile.of(this.profileRepository.save(new ProfileEntity(uniqueId, profileType.id())));
    });
  }

  /**
   * See {@link IProfileDatabase#getProfile(String)}.
   */
  @Override
  public @NotNull CompletableResponse<IProfile> getProfile(@Nullable String uniqueId) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null checks
      Objects.requireNonNull(uniqueId);

      return this.profileRepository.findByUniqueId(uniqueId)
        .map(ImmutableProfile::of)
        .orElse(null);
    });
  }

  /**
   * See {@link IProfileDatabase#setAttribute(String, String, String)}.
   */
  @Override
  public @NotNull CompletableResponse<IProfileAttribute> setAttribute(@Nullable String uniqueId,
                                                                      @Nullable String key,
                                                                      @Nullable String value) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null checks
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(key);

      final ProfileEntity profileEntity = this.profileRepository
        .findByUniqueId(uniqueId)
        .orElseThrow(() -> {
          LOGGER.error("No profile with uniqueId='{}' found to set attribute.", uniqueId);
          return new NullPointerException();
        });

      /*
       * Get attribute else null.
       */
      final ProfileAttributeEntity profileAttributeEntity = this.profileAttributeRepository
        .findByProfileAndKey(profileEntity, key)
        .orElse(null);


      if (profileAttributeEntity == null && value == null) {
        //Nothing to do here.
        return null;
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
      return ImmutableProfileAttribute.of(this.profileAttributeRepository.save(new ProfileAttributeEntity(profileEntity, key, value)));
    });
  }

  /**
   * See {@link IProfileDatabase#removeAttribute(String, String)}.
   */
  @Override
  public @NotNull CompletableResponse<IProfileAttribute> removeAttribute(@Nullable String uniqueId,
                                                                         @Nullable String key) {
    return this.setAttribute(uniqueId, key, null);
  }

  /**
   * See {@link IProfileDatabase#getAttributes(String)}.
   */
  @Override
  public @NotNull CompletableResponse<List<IProfileAttribute>> getAttributes(@Nullable String uniqueId) {
    return SpaceLibrary.completeResponseAsync(() -> {
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
        .orElseThrow();
    });
  }

  /**
   * See {@link IProfileDatabase#getAttribute(String, String)}.
   */
  @Override
  public @NotNull CompletableResponse<IProfileAttribute> getAttribute(@Nullable String uniqueId,
                                                                      @Nullable String key) {
    return SpaceLibrary.completeResponseAsync(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(key);

      /*
       * Search in database.
       */
      return this.profileRepository.findByUniqueId(uniqueId)
        .flatMap(profileEntity -> this.profileAttributeRepository.findByProfileAndKey(profileEntity, key))
        .map(ImmutableProfileAttribute::of)
        .orElseThrow();
    });
  }
}
