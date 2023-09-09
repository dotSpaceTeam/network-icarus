package dev.dotspace.network.node.profile.db.experience;

import dev.dotspace.common.response.Response;
import dev.dotspace.network.library.profile.experience.IExperience;
import dev.dotspace.network.library.profile.experience.IExperienceManipulator;
import dev.dotspace.network.library.profile.experience.ImmutableExperience;
import dev.dotspace.network.node.database.AbstractDatabase;
import dev.dotspace.network.node.profile.db.ProfileEntity;
import dev.dotspace.network.node.profile.db.ProfileRepository;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@Log4j2
public final class ExperienceDatabase extends AbstractDatabase implements IExperienceManipulator {
  /**
   * Instance to communicate tp profiles.
   */
  @Autowired
  private ProfileRepository profileRepository;

  /**
   * Instance to communicate to experience.
   */
  @Autowired
  private ExperienceRepository experienceRepository;


  @Override
  public @NotNull Response<IExperience> addExperience(@Nullable String uniqueId,
                                                      @Nullable String experienceName,
                                                      long value) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(experienceName);

      final ProfileEntity profileEntity = this.profileRepository
          //Search for uuid, throw error if absent.
          .findByUniqueId(uniqueId)
          //Error.
          .orElseThrow(this.failOptional("No profile for "+uniqueId+"found, can't add experience value."));

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
    });
  }

  @Override
  public @NotNull Response<IExperience> getExperience(@Nullable String uniqueId,
                                                      @Nullable String experienceName) {
    return this.responseService().response(() -> {
      Objects.requireNonNull(uniqueId);
      Objects.requireNonNull(experienceName);

      final ProfileEntity profileEntity = this.profileRepository
          //Search for uuid, throw error if absent.
          .findByUniqueId(uniqueId)
          //Error.
          .orElseThrow(this.failOptional("No profile for "+uniqueId+"found, can't get experience."));

      return this.experienceRepository
          .findByProfileAndName(profileEntity, experienceName)
          .orElseThrow(this.failOptional("No experience found."));
    });
  }

  @Override
  public @NotNull Response<IExperience> getTotalExperience(@Nullable String uniqueId) {
    return this.responseService().response(() -> {
      Objects.requireNonNull(uniqueId);

      final ProfileEntity profileEntity = this.profileRepository
          //Search for uuid, throw error if absent.
          .findByUniqueId(uniqueId)
          //Error.
          .orElseThrow(this.failOptional("No profile for "+uniqueId+"found, can't get experience."));

      return ImmutableExperience.of(this.experienceRepository
              .getTotal(profileEntity.id())
              .orElseThrow(this.failOptional("No total value found for experience.")),
          Function.function());
    });
  }
}
