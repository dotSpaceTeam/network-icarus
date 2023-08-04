package dev.dotspace.network.library.spigot.self;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import dev.dotspace.network.library.game.scoreboard.GameSidebarProvider;
import dev.dotspace.network.library.spigot.scoreboard.SidebarProvider;

/**
 * Icarus module
 */
public final class IcarusModule extends AbstractModule {
    /**
     * Configure Icarus plugin
     */
    @Override
    protected void configure() {
        //Configure sidebar provider.
        this.bind(GameSidebarProvider.class)
                .to(SidebarProvider.class)
                .in(Scopes.SINGLETON);
    }
}
