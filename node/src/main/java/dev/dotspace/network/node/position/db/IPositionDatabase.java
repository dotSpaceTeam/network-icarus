package dev.dotspace.network.node.position.db;

import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.network.library.position.IPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IPositionDatabase {

  @NotNull CompletableResponse<IPosition> setPosition(@Nullable final String key,
                                                      final long x,
                                                      final long y,
                                                      final long z);

  @NotNull CompletableResponse<IPosition> getPosition(@Nullable final String key);
}
