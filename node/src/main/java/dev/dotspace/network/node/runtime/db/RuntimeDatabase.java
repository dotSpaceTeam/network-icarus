package dev.dotspace.network.node.runtime.db;

import dev.dotspace.common.response.Response;
import dev.dotspace.network.library.runtime.IRuntime;
import dev.dotspace.network.library.runtime.IRuntimeManipulator;
import dev.dotspace.network.library.runtime.ImmutableRuntime;
import dev.dotspace.network.library.runtime.RuntimeType;
import dev.dotspace.network.node.database.AbstractDatabase;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component("runtimeDatabase")
@Log4j2
public final class RuntimeDatabase extends AbstractDatabase implements IRuntimeManipulator {
  /**
   * Manipulate data.
   */
  @Autowired
  private RuntimeRepository runtimeRepository;

  @Override
  public @NotNull Response<IRuntime> createRuntime(@Nullable String id,
                                                   @Nullable RuntimeType runtimeType) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(id);
      Objects.requireNonNull(runtimeType);

      //Check if already present.
      if (this.runtimeRepository.findByRuntimeId(id).isPresent()) {
        log.warn("Error while creating runtime={} (type={}).", id, runtimeType);
        throw new NullPointerException("Already present.");
      }

      final RuntimeEntity runtimeEntity = new RuntimeEntity(id, runtimeType);

      this.runtimeRepository.save(runtimeEntity); //Store.
      log.debug("Stored runtime id={}.", id);

      return ImmutableRuntime.of(runtimeEntity);
    });
  }

  @Override
  public @NotNull Response<IRuntime> getRuntime(@Nullable String id) {
    return this.responseService().response(() -> {
      //Null check
      Objects.requireNonNull(id);

      return this.runtimeRepository
          .findByRuntimeId(id)
          .map(ImmutableRuntime::of)
          .orElse(null);
    });
  }
}
