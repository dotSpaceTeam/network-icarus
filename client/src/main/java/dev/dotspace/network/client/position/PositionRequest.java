package dev.dotspace.network.client.position;

import dev.dotspace.common.SpaceLibrary;
import dev.dotspace.common.response.CompletableResponse;
import dev.dotspace.network.client.web.AbstractRequest;
import dev.dotspace.network.library.position.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class PositionRequest extends AbstractRequest implements IPositionRequest {
    /**
     * See {@link IPositionManipulator#setPosition(String, long, long, long)}.
     */
    @Override
    public @NotNull CompletableResponse<IPosition> setPosition(@Nullable String key,
                                                               long x,
                                                               long y,
                                                               long z) {
        return SpaceLibrary.completeResponseAsync(() -> {
            //Null check
            Objects.requireNonNull(key);

            return this.client().put("/v1/position", ImmutablePosition.class, new ImmutablePosition(key, x, y, z));
        });
    }

    /**
     * See {@link IPositionManipulator#getPosition(String)}.
     */
    @Override
    public @NotNull CompletableResponse<IPosition> getPosition(@Nullable String key) {
        return SpaceLibrary.completeResponseAsync(() -> {
            //Null check
            Objects.requireNonNull(key);

            return this.client().get("/v1/position/%s".formatted(key), ImmutablePosition.class);
        });
    }

    /**
     * See {@link IPositionManipulator#setViewPosition(String, long, long, long, long, long)}.
     */
    @Override
    public @NotNull CompletableResponse<IViewPosition> setViewPosition(@Nullable String key,
                                                                       long x,
                                                                       long y,
                                                                       long z,
                                                                       long yaw,
                                                                       long pitch) {
        return SpaceLibrary.completeResponseAsync(() -> {
            //Null check
            Objects.requireNonNull(key);

            return this.client().put("/v1/position/view",
                    ImmutableViewPosition.class, new ImmutableViewPosition(key, x, y, z, yaw, pitch));
        });
    }

    /**
     * See {@link IPositionManipulator#setViewPosition(String, long, long)}.
     */
    @Override
    public @NotNull CompletableResponse<IViewPosition> setViewPosition(@Nullable String key,
                                                                       long yaw,
                                                                       long pitch) {
        return CompletableResponse.exceptionally(new Exception("Not implemented."));
    }

    /**
     * See {@link IPositionManipulator#getViewPosition(String)}.
     */
    @Override
    public @NotNull CompletableResponse<IViewPosition> getViewPosition(@Nullable String key) {
        return SpaceLibrary.completeResponseAsync(() -> {
            //Null check
            Objects.requireNonNull(key);

            return this.client().get("/v1/position/view/%s".formatted(key), ImmutableViewPosition.class);
        });
    }
}
