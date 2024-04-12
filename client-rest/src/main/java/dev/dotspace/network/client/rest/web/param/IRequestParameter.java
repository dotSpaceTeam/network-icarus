package dev.dotspace.network.client.rest.web.param;

import dev.dotspace.network.library.key.IKey;
import org.jetbrains.annotations.NotNull;


public interface IRequestParameter extends IKey {

  @NotNull String parameter();

}
