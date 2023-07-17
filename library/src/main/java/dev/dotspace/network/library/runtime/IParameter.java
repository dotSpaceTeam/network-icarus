package dev.dotspace.network.library.runtime;

import dev.dotspace.network.library.key.IKey;
import dev.dotspace.network.library.timestamp.ITimestamp;
import org.jetbrains.annotations.NotNull;

public interface IParameter<TYPE> extends IKey, ITimestamp {
  @NotNull TYPE value();

}
