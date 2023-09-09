package dev.dotspace.network.node.runtime.db;

import dev.dotspace.network.library.runtime.IRuntime;
import dev.dotspace.network.library.runtime.ImmutableRuntime;
import dev.dotspace.network.library.runtime.RuntimeType;
import dev.dotspace.network.node.database.AbstractDatabase;
import dev.dotspace.network.node.exception.ElementAlreadyPresentException;
import dev.dotspace.network.node.exception.ElementNotPresentException;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component("runtimeDatabase")
@Log4j2
public final class RuntimeDatabase extends AbstractDatabase {
  /**
   * Manipulate data.
   */
  @Autowired
  private RuntimeRepository runtimeRepository;

  public @NotNull IRuntime createRuntime(@Nullable String id,
                                         @Nullable RuntimeType runtimeType) throws ElementAlreadyPresentException {
    //Null check
    Objects.requireNonNull(id);
    Objects.requireNonNull(runtimeType);

    //Check if already present.
    if (this.runtimeRepository.findByRuntimeId(id).isPresent()) {
      throw new ElementAlreadyPresentException(null, "Session id="+id+" already present.");
    }

    final RuntimeEntity runtimeEntity = new RuntimeEntity(id, runtimeType);

    this.runtimeRepository.save(runtimeEntity); //Store.
    log.debug("Stored runtime id={}.", id);

    return ImmutableRuntime.of(runtimeEntity);
  }

  public @NotNull IRuntime getRuntime(@Nullable String id) throws ElementNotPresentException {
    //Null check
    Objects.requireNonNull(id);

    return this.runtimeRepository
        .findByRuntimeId(id)
        .map(ImmutableRuntime::of)
        .orElseThrow(() ->
            new ElementNotPresentException(null, "No session found for id="+id+"."));
  }
}
