package dev.dotspace.network.library.velocity.self;

import com.google.inject.AbstractModule;
import dev.dotspace.network.library.velocity.self.session.ISessionMap;
import dev.dotspace.network.library.velocity.self.session.SessionMap;


/**
 * Module for locale velocity plugin.
 */
public final class SelfModule extends AbstractModule {

  @Override
  protected void configure() {
    //Bind locale session map.
    this.bind(ISessionMap.class).toInstance(new SessionMap());
  }
}
