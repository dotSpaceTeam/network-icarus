package dev.dotspace.network.node.prohibit.db;

import dev.dotspace.network.library.data.DataManipulation;
import dev.dotspace.network.library.prohibit.IProhibitReason;
import dev.dotspace.network.library.prohibit.ImmutableProhibitReason;
import dev.dotspace.network.library.prohibit.ProhibitType;
import dev.dotspace.network.node.database.AbstractDatabase;
import dev.dotspace.network.node.database.exception.IllegalEntityException;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Database to manipulate prohibits.
 */
@Component
@Log4j2
public final class ProhibitDatabase extends AbstractDatabase {
  /**
   * Instance to communicate to prohibit reason.
   */
  @Autowired
  private ProhibitReasonRepository prohibitReasonRepository;

  /**
   * Update reason.
   */
  public @NotNull IProhibitReason updateReason(@Nullable final ProhibitType prohibitType,
                                               @Nullable final String name,
                                               @Nullable final String title,
                                               @Nullable final String description) throws IllegalEntityException {
    //Null checks
    Objects.requireNonNull(prohibitType);
    Objects.requireNonNull(name);
    Objects.requireNonNull(title);
    Objects.requireNonNull(description);

    //Get entity if present else null.
    @Nullable final ProhibitReasonEntity namedReasonEntity = this
        .prohibitReasonRepository
        //Get name from entity.
        .findByNameIgnoreCase(name)
        //Else null.
        .orElse(null);

    //Update present entity.
    if (namedReasonEntity != null) {
      //Check if type is same that already exits.
      if (prohibitType != namedReasonEntity.type()) {
        //If not throw error -> no update.
        throw new IllegalEntityException(namedReasonEntity, "Can't update reason="+name+" because type mismatch.");
      }

      //Update title and description of present object.
      namedReasonEntity.title(title).description(description);

      //Updated object.
      final IProhibitReason updatedReason =
          ImmutableProhibitReason.of(this.prohibitReasonRepository.save(namedReasonEntity));

      //Fire event -> update
      this.publishEvent(prohibitType, ImmutableProhibitReason.class, DataManipulation.UPDATE);

      //Return
      return updatedReason;
    }

    //Create new entity with method parameters.
    final IProhibitReason createdReason = ImmutableProhibitReason
        .of(this.prohibitReasonRepository.save(new ProhibitReasonEntity(prohibitType, name, title, description)));

    //Fire event -> create
    this.publishEvent(createdReason, ImmutableProhibitReason.class, DataManipulation.CREATE);

    //Return created.
    return createdReason;
  }
}
