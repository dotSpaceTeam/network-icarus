package dev.dotspace.network.library.game.bridge;

import dev.dotspace.network.library.context.IContext;
import dev.dotspace.network.library.profile.ProfileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IConnectContext extends IContext<Boolean> {

  @NotNull IConnectContext type(@Nullable final ProfileType profileType);

  @NotNull IConnectContext uniqueId(@Nullable final String uniqueId);

  @NotNull IConnectContext name(@Nullable final String name);

  @NotNull IConnectContext texture(@Nullable final String texture);

  @NotNull IConnectContext signature(@Nullable final String signature);
}
