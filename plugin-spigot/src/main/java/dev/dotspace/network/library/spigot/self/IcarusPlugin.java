package dev.dotspace.network.library.spigot.self;

import dev.dotspace.network.client.Client;
import dev.dotspace.network.library.game.plugin.GameDisableConfiguration;
import dev.dotspace.network.library.game.plugin.GameEnableConfiguration;
import dev.dotspace.network.library.game.plugin.GameLoadConfiguration;
import dev.dotspace.network.library.spigot.plugin.AbstractPlugin;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

@Log4j2
public final class IcarusPlugin extends AbstractPlugin {
    /**
     * See {@link AbstractPlugin#load(GameLoadConfiguration)}
     */
    @Override
    public void load(@NotNull GameLoadConfiguration configuration) {
        log.info("Loading ikarus spigot.");

        configuration.addModule(new IcarusModule());

        log.info("Loaded ikarus spigot.");
    }

    /**
     * {@link AbstractPlugin#enable(GameEnableConfiguration)}
     */
    @Override
    public void enable(@NotNull GameEnableConfiguration configuration) {
        //Enable client.
        Client.enable();
    }

    /**
     * {@link AbstractPlugin#disable(GameDisableConfiguration)}
     */
    @Override
    public void disable(@NotNull GameDisableConfiguration configuration) {

    }
}
