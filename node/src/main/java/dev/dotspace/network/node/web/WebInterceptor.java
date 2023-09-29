package dev.dotspace.network.node.web;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalCause;
import dev.dotspace.network.library.connection.IRestRequest;
import dev.dotspace.network.library.connection.ImmutableRestRequest;
import dev.dotspace.network.library.field.RequestField;
import dev.dotspace.network.node.exception.ElementAlreadyPresentException;
import dev.dotspace.network.node.web.event.ClientAddEvent;
import dev.dotspace.network.node.web.event.ClientRemoveEvent;
import dev.dotspace.network.node.web.event.RequestResponseEvent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

/**
 * Class to hook in front and between operation.
 */
@Component
@Log4j2
public final class WebInterceptor implements HandlerInterceptor {
  /**
   * Spring event driver.
   */
  @Autowired
  private ApplicationEventPublisher applicationEventPublisher;
  /**
   * Cache for clients.
   */
  private final @NotNull Cache<String, Long> clientCache;

  public WebInterceptor() {
    this.clientCache = CacheBuilder.newBuilder()
        .expireAfterAccess(Duration.ofSeconds(15))
        .removalListener(notification -> {
          //Return if not expired.
          if (notification.getCause() != RemovalCause.EXPIRED) {
            return;
          }
          //Get client id.
          final String clientId = (String) notification.getKey();

          //Ignore if client is null.
          if (clientId == null) {
            return;
          }

          //Run event to remove
          this.applicationEventPublisher.publishEvent(new ClientRemoveEvent(this, clientId));
        })
        .build();
  }

  /**
   * Before controller handle.
   */
  @Override
  public boolean preHandle(@NotNull final HttpServletRequest request,
                           @NotNull final HttpServletResponse response,
                           @NotNull final Object handler) throws Exception {
    //Get client id
    @Nullable final String clientId = request.getHeader(RequestField.CLIENT_ID);

    //Pass client register.
    this.handleClientRegister(clientId);

    //Set current timestamp as start
    request.setAttribute("process-begin", System.currentTimeMillis());

    return true;
  }

  /**
   * After controller handle.
   */
  @Override
  public void postHandle(@NotNull final HttpServletRequest request,
                         @NotNull final HttpServletResponse response,
                         @NotNull final Object handler,
                         @Nullable final ModelAndView modelAndView) throws Exception {
  //Nothing.
  }

  @Override
  public void afterCompletion(@NotNull final HttpServletRequest request,
                              @NotNull final HttpServletResponse response,
                              @NotNull final Object handler,
                              @Nullable final Exception exception) throws Exception {
    //Build request
    final IRestRequest restRequest = new ImmutableRestRequest(
        request.getRequestURI(),
        //Client header else empty name.
        Optional.ofNullable(request.getHeader(RequestField.CLIENT_ID)).orElse(""),
        //Get method
        request.getMethod(),
        //Get address
        request.getRemoteAddr(),
        exception != null ? exception.getMessage() : "",
        new Date(),
        //Calculate duration
        System.currentTimeMillis()-(long) request.getAttribute("process-begin"),
        response.getStatus()
    );

    this.applicationEventPublisher.publishEvent(new RequestResponseEvent(this, restRequest));
  }

  private void handleClientRegister(@Nullable final String clientId)
      throws ElementAlreadyPresentException {
    //Client absent -> return
    if (clientId == null) {
      return;
    }

    //Already present.
    if (this.clientCache.getIfPresent(clientId) != null) {
      return;
    }

    //Add to clients.
    this.clientCache.put(clientId, System.currentTimeMillis());

    //Run event to add
    this.applicationEventPublisher.publishEvent(new ClientAddEvent(this, clientId));
  }
}
