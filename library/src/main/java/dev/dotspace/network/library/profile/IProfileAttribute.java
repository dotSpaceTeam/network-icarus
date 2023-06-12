package dev.dotspace.network.library.profile;

import dev.dotspace.network.library.key.IKey;
import org.jetbrains.annotations.NotNull;

public interface IProfileAttribute extends IKey {

     @NotNull String value();

}
