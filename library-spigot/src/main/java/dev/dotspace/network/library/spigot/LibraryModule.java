package dev.dotspace.network.library.spigot;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import dev.dotspace.network.library.spigot.scoreboard.ISidebarProvider;
import dev.dotspace.network.library.spigot.scoreboard.SidebarProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE) /*Block constructor*/
public final class LibraryModule extends AbstractModule {

    private final static @NotNull LibraryModule instance;

    static {
        instance = new LibraryModule();
        log.info("Create singleton LibraryModule.");
    }

    /**
     * Configure library module.
     */
    @Override
    protected void configure() {
        //Configure sidebar provider.
        this.bind(ISidebarProvider.class)
                .to(SidebarProvider.class)
                .in(Scopes.SINGLETON);
        log.info("Defined SidebarProvider.");
    }

    //static

    public static @NotNull LibraryModule instance() {
        return instance;
    }
}
