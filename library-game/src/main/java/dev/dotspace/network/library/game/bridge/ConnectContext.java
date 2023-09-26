package dev.dotspace.network.library.game.bridge;

import dev.dotspace.common.function.ThrowableConsumer;
import dev.dotspace.common.response.Response;
import dev.dotspace.network.client.Client;
import dev.dotspace.network.library.context.IContext;
import dev.dotspace.network.library.exception.LibraryException;
import dev.dotspace.network.library.profile.ProfileType;
import dev.dotspace.network.library.session.ISession;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Log4j2
@Setter
@Accessors(fluent=true)
public class ConnectContext implements IConnectContext {
  private @Nullable ProfileType type;
  private @Nullable String uniqueId;
  private @Nullable String name;
  private @Nullable String texture;
  private @Nullable String signature;

  @Override
  public @NotNull Boolean forceComplete() {

      return false;
  }

  @Override
  public @NotNull Response<Boolean> complete() {
    return null;
  }

  @Override
  public @NotNull IContext<Boolean> handle(@Nullable ThrowableConsumer<Boolean> handleConsumer) {
    return null;
  }
}
