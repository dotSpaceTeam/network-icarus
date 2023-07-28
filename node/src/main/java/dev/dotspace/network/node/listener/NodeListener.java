package dev.dotspace.network.node.listener;

import dev.dotspace.network.node.runtime.db.RestRequestDatabase;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Executor;

@Component
@Log4j2
public final class NodeListener {

  /**
   *
   */
  @Autowired
  private RestRequestDatabase database;

  @Autowired
  private Executor executor;

  @EventListener
  public void handleEvent(@NotNull final ServletRequestHandledEvent event) {
    //Store request parameters.
    final String requestUrl = event.getRequestUrl();
    final String method = event.getMethod();
    final long time = event.getProcessingTimeMillis();

    //Log
    log.info("{}-Request '{}' done after {} ms.", method, requestUrl, time);

    //Add to database.
    this.executor.execute(() -> {
      //Log request.
      this.database.createRequestInfo(
        requestUrl,
        event.getClientAddress(),
        method,
        time,
        //Invert value.
        !event.wasFailure(),
        Optional.ofNullable(event.getFailureCause()).map(Throwable::getMessage).orElse(null),
        new Date());
    });
  }
}
