package dev.dotspace.network.node.position.db;

import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.network.library.position.IPosition;
import dev.dotspace.network.library.position.IViewPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface IPositionDatabase {

  @NotNull CompletableResponse<IPosition> setPosition(@Nullable final String key,
                                                      final long x,
                                                      final long y,
                                                      final long z);

  @NotNull CompletableResponse<IPosition> getPosition(@Nullable final String key);

  @NotNull CompletableResponse<IViewPosition> setViewPosition(@Nullable final String key,
                                                              final long x,
                                                              final long y,
                                                              final long z,
                                                              final long yaw,
                                                              final long pitch);

  @NotNull CompletableResponse<IViewPosition> setViewPosition(@Nullable final String key,
                                                              final long yaw,
                                                              final long pitch);
}
