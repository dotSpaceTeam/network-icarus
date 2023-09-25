package dev.dotspace.network.library.spigot;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import dev.dotspace.network.library.provider.Provider;
import dev.dotspace.network.library.spigot.inventory.IInventoryProvider;
import dev.dotspace.network.library.spigot.inventory.InventoryProvider;
import dev.dotspace.network.library.spigot.itemstack.IItemProvider;
import dev.dotspace.network.library.spigot.itemstack.ItemProvider;
import dev.dotspace.network.library.spigot.scoreboard.ISidebarProvider;
import dev.dotspace.network.library.spigot.scoreboard.SidebarProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;


@Log4j2
@NoArgsConstructor(access=AccessLevel.PRIVATE) /*Block constructor*/
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
    //Register sidebar provider
    this.bindProvider(ISidebarProvider.class, SidebarProvider.class);

    //Register inventory provider
    this.bindProvider(IInventoryProvider.class, InventoryProvider.class);

    //Register item provider
    this.bindProvider(IItemProvider.class, ItemProvider.class);
  }

  private <PROVIDER> void bindProvider(@NotNull final Class<PROVIDER> interfaceClass,
                                       @NotNull final Class<? extends PROVIDER> implementationClass) {
    //Bind provider.
    this.bind(interfaceClass).to(implementationClass).in(Scopes.SINGLETON);
    log.info("Bound provider={} to implementation={}.", interfaceClass, implementationClass);
  }

  //static

  public static @NotNull LibraryModule instance() {
    return instance;
  }
}
