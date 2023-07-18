package dev.dotspace.network.library.session;

import dev.dotspace.common.response.CompletableResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ISessionManipulator {

  @NotNull CompletableResponse<List<ISession>> getSessionList(@Nullable final String uniqueId);

  @NotNull CompletableResponse<ISession> getSession(@Nullable final String uniqueId,
                                                    @Nullable final Long sessionId);

  @NotNull CompletableResponse<IPlaytime> getPlaytime(@Nullable final String uniqueId);

  @NotNull CompletableResponse<ISession> createSession(@Nullable final String uniqueId);

  @NotNull CompletableResponse<ISession> completeSession(@Nullable final String uniqueId,
                                                         @Nullable final Long sessionId);

}
