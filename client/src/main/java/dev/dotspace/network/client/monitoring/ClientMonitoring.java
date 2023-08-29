package dev.dotspace.network.client.monitoring;

import com.google.inject.Inject;
import dev.dotspace.common.function.ThrowableRunnable;
import dev.dotspace.common.response.Response;
import dev.dotspace.common.response.ResponseService;
import dev.dotspace.network.client.Client;
import dev.dotspace.network.client.ClientState;
import dev.dotspace.network.library.common.StateHandler;
import dev.dotspace.network.library.common.StateMap;
import dev.dotspace.network.library.state.ImmutableBooleanState;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


/**
 * Class to maintain health of client.
 */
@Log4j2
@Accessors(fluent = true)
public final class ClientMonitoring implements StateHandler<ClientState> {
  //################## Config values ######################
  /**
   * Time between pings.
   */
  private final static long PING_INTERVAL = 2000L;

  /**
   * Response service to handle responses.
   */
  @Inject
  private ResponseService responseService;

  private long totalPingCount;
  private long lastStateChange;
  @Getter
  private @NotNull ClientState clientState;

  /**
   * Store values of state runnable.
   */
  private final @NotNull StateMap<ClientState> stateMap;

  /**
   * Construct instance.
   */
  public ClientMonitoring() {
    this.stateMap = StateMap.createMap();
    this.clientState = ClientState.FAILED;

    log.info("Starting client monitoring...");
    //Start new thread.
    new Thread(() -> {
      //Run as long client runs.
      while (!Client.client().thread().isInterrupted()) {
        //Ping if time is positive.
        if (this.lastStateChange-System.currentTimeMillis()<=0) {
          //Set new ping
          try {
            this.ping().get();
            this.totalPingCount++;
          } catch (final InterruptedException ignore) {
          }
        }

        try {
          //To keep performance height
          Thread.sleep(500L);
          //Ignore.
        } catch (final InterruptedException ignore) {
        }
      }
      //Kill thread if main is also shut down.
      log.info("Client monitoring stopped.");
    }).start();
  }

  /**
   * Ping master.
   *
   * @return
   */
  private @NotNull Response<Long> ping() {
    //If client is not active.
    if (!Client.enabled()) {
      //Return request with error.
      return this.responseService.response(() -> {
        throw new IllegalStateException("Client is not active, no ping possible.");
      });
    }

    //Time enabled.
    final long start = System.currentTimeMillis();

    return this.responseService.response(() -> {
      final ImmutableBooleanState state = Client
          .client()
          .statusRequest()
          .getState()
          .get();

      final long end = (System.currentTimeMillis()-start);

      if (state != null) {
        log.info("API endpoint available(Status={}). Took {}ms.", state.state(), end);
        this.lastState(ClientState.ESTABLISHED);
      } else {
        log.warn("No response from API endpoint. Took {}ms.", end);
        this.lastState(ClientState.FAILED);
      }

      return end;
    });
  }

  public void lastState(@Nullable final ClientState lastState) {
    //Null check
    Objects.requireNonNull(lastState);

    this.lastStateChange = System.currentTimeMillis()+PING_INTERVAL;
    //Ignore change
    if (this.clientState == lastState && this.totalPingCount>0) {
      return;
    }
    this.clientState = lastState;

    if (this.clientState == ClientState.ESTABLISHED) {
      log.info("Client connection established.");

      this.stateMap.executeRunnable(ClientState.ESTABLISHED);
      return;
    }
    log.warn("Client connection failed.");
    this.stateMap.executeRunnable(ClientState.FAILED);
  }


  @Override
  public @NotNull StateHandler<ClientState> handle(@Nullable ClientState clientState,
                                                   @Nullable ThrowableRunnable runnable) {
    Objects.requireNonNull(clientState);
    Objects.requireNonNull(runnable);

    this.stateMap.append(clientState, runnable);
    return this;
  }
}
