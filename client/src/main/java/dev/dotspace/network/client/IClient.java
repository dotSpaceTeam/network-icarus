package dev.dotspace.network.client;

import dev.dotspace.network.client.position.IPositionRequest;
import dev.dotspace.network.client.profile.IProfileRequest;
import dev.dotspace.network.client.session.ISessionRequest;
import dev.dotspace.network.client.status.IStatusRequest;
import org.jetbrains.annotations.NotNull;

public interface IClient {
    /**
     * Get status of api point.
     *
     * @return instance of {@link IStatusRequest}.
     */
    @NotNull IStatusRequest statusRequest();

    /**
     * Get request instance of client.
     *
     * @return instance of {@link IProfileRequest}.
     */
    @NotNull IProfileRequest profileRequest();

    /**
     * Get request instance of client.
     *
     * @return instance of {@link IPositionRequest}.
     */
    @NotNull IPositionRequest positionRequest();

    /**
     * Get request instance of client.
     *
     * @return instance of {@link ISessionRequest}.
     */
    @NotNull ISessionRequest sessionRequest();
}