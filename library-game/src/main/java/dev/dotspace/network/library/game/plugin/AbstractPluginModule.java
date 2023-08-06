package dev.dotspace.network.library.game.plugin;

import com.google.inject.AbstractModule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Abstract module for all extensions
 */
@Log4j2
@Accessors(fluent = true)
public abstract class AbstractPluginModule<PLUGIN> extends AbstractModule {

    /**
     * Plugin of module.
     */
    @Getter(AccessLevel.PROTECTED)
    private final @NotNull PLUGIN plugin;

    /**
     * Construct.
     *
     * @param plugin to set as {@link AbstractPluginModule#plugin}
     * @throws NullPointerException if plugin is null.
     */
    public AbstractPluginModule(@Nullable final PLUGIN plugin) {
        this.plugin = Objects.requireNonNull(plugin);
    }
}
