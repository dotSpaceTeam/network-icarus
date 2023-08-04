package dev.dotspace.network.client;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.dotspace.network.client.position.IPositionRequest;
import dev.dotspace.network.client.position.PositionRequest;
import dev.dotspace.network.client.profile.IProfileRequest;
import dev.dotspace.network.client.profile.ProfileRequest;
import dev.dotspace.network.client.session.ISessionRequest;
import dev.dotspace.network.client.session.SessionRequest;
import dev.dotspace.network.client.status.IStatusRequest;
import dev.dotspace.network.client.status.StatusRequest;
import dev.dotspace.network.client.web.AbstractRequest;
import dev.dotspace.network.library.provider.IProviderManager;
import dev.dotspace.network.library.provider.Provider;
import dev.dotspace.network.library.provider.ProviderManager;
import dev.dotspace.network.library.runtime.IRuntime;
import dev.dotspace.network.library.runtime.ImmutableRuntime;
import dev.dotspace.network.library.runtime.RuntimeType;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Log4j2
public final class Client implements IClient {
    /**
     * Runtime info.
     */
    private final @NotNull IRuntime runtime;
    /**
     * Manager to hold provider for client.
     */
    private final @NotNull Injector injector;

    /**
     * Only local .
     */
    private Client() {
        this.runtime = ImmutableRuntime.randomOfType(RuntimeType.CLIENT);
        //Create injector.
        this.injector = Guice.createInjector(new ClientModule(this.runtime.runtimeId()));

        log.info("Instance running under id={}.", this.runtime.runtimeId());
    }

    /**
     * See {@link IClient#statusRequest()}
     */
    @Override
    public @NotNull IStatusRequest statusRequest() {
        return this.request(IStatusRequest.class);
    }

    /**
     * See {@link IClient#profileRequest()}
     */
    @Override
    public @NotNull IProfileRequest profileRequest() {
        return this.request(IProfileRequest.class);
    }

    /**
     * See {@link IClient#profileRequest()}
     */
    @Override
    public @NotNull IPositionRequest positionRequest() {
        return this.request(IPositionRequest.class);
    }

    /**
     * See {@link IClient#sessionRequest()}
     */
    @Override
    public @NotNull ISessionRequest sessionRequest() {
        return this.request(ISessionRequest.class);
    }

    //Get provider else throw
    private <REQUEST> @NotNull REQUEST request(@NotNull final Class<REQUEST> requestClass) {
        return this.injector.getInstance(requestClass);
    }

    //static
    private final static @NotNull Client client = new Client();
    //Check if client is enabled.
    private static boolean enabled = false;

    /**
     * Get client instance.
     *
     * @return get singleton instance.
     */
    public static @NotNull IClient client() {
        return client;
    }

    /**
     * Enable if not enabled.
     */
    public static void enable() {
        //Already enabled.
        if (enabled) {
            log.warn("Client already enabled.");
            return;
        }

        //Init client
        enabled = true;
        log.info("Enabled client.");
        log.info("Checking client status...");

        //Time enabled.
        final long start = System.currentTimeMillis();
        //Send request to api
        client
                .statusRequest()
                .getState()
                //All fine.
                .ifPresent(state ->
                        log.info("API endpoint available(Status={}). Took {}ms.", state.state(), (System.currentTimeMillis() - start)))
                //If request is null.
                .ifAbsent(() -> log.warn("Empty response from endpoint. Took {}ms.", (System.currentTimeMillis() - start)))
                //If api request fails.
                .ifExceptionally(throwable ->
                        log.error("Error while contacting endpoint({}). Took {}ms.",
                                Optional
                                        .ofNullable(throwable)
                                        .map(Throwable::getMessage)
                                        .orElse(null), (System.currentTimeMillis() - start)));
        //End of request
    }
}
