package dev.dotspace.network.node.profile.db.experience;

import dev.dotspace.network.library.profile.experience.IExperience;
import dev.dotspace.network.library.profile.experience.ImmutableExperience;
import dev.dotspace.network.node.database.AbstractDatabase;
import dev.dotspace.network.node.exception.ElementException;
import dev.dotspace.network.node.profile.db.ProfileEntity;
import dev.dotspace.network.node.profile.db.ProfileRepository;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;


@Component
@Log4j2
public final class ExperienceDatabase extends AbstractDatabase {
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


  public @NotNull IExperience addExperience(@Nullable String uniqueId,
                                            @Nullable String experienceName,
                                            long value) throws ElementException {
    //Null check
    Objects.requireNonNull(uniqueId);
    Objects.requireNonNull(experienceName);

    final ProfileEntity profileEntity = this.profileRepository
        //Search for uuid, throw error if absent.
        .profileElseThrow(uniqueId, "No profile for "+uniqueId+"found, can't add experience value.");

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

  public @NotNull IExperience getExperience(@Nullable String uniqueId,
                                            @Nullable String experienceName) throws ElementException {
    Objects.requireNonNull(uniqueId);
    Objects.requireNonNull(experienceName);

    final ProfileEntity profileEntity = this.profileRepository
        //Search for uuid, throw error if absent.
        .profileElseThrow(uniqueId, "No profile for "+uniqueId+"found, can't get experience.");

    return ImmutableExperience.of(this.experienceRepository
        .findByProfileAndName(profileEntity, experienceName)
        .orElseThrow(this.failOptional("No experience found.")));
  }

  public @NotNull List<IExperience> getExperienceList(@Nullable String uniqueId) throws ElementException {
    final ProfileEntity profileEntity = this.profileRepository
        //Search for uuid, throw error if absent.
        .profileElseThrow(uniqueId, "No profile for "+uniqueId+"found, can't get experience.");

    return this.experienceRepository
        .findByProfile(profileEntity)
        .stream()
        .map(ImmutableExperience::of)
        .toList();
  }

  public @NotNull IExperience getTotalExperience(@Nullable String uniqueId) throws ElementException {
    Objects.requireNonNull(uniqueId);

    final ProfileEntity profileEntity = this.profileRepository
        //Search for uuid, throw error if absent.
        .profileElseThrow(uniqueId, "No profile for "+uniqueId+"found, can't get experience.");

    return ImmutableExperience.of(
        "Total",
        this.experienceRepository
            .getTotal(profileEntity.id())
            .orElseThrow(this.failOptional("No total value found for experience.")),
        Function.function());
  }
}
