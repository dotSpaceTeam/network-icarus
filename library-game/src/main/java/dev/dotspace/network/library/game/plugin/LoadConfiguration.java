package dev.dotspace.network.library.game.plugin;

import com.google.common.collect.ImmutableList;
import com.google.inject.Module;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoadConfiguration implements GameLoadConfiguration {
    private final @NotNull List<Module> modules;

    public LoadConfiguration() {
        this.modules = new ArrayList<>();
    }

    /**
     * See {@link GameLoadConfiguration#addModule(Module)}
     */
    @Override
    public @NotNull GameLoadConfiguration addModule(@Nullable Module module) {
        this.modules.add(Objects.requireNonNull(module));
        return this;
    }

    /**
     * See {@link GameLoadConfiguration#removeModule(Module)}
     */
    @Override
    public @NotNull GameLoadConfiguration removeModule(@Nullable Module module) {
        this.modules.remove(Objects.requireNonNull(module));
        return this;
    }

    /**
     * See {@link GameLoadConfiguration#modules()}
     */
    @Override
    public @NotNull List<Module> modules() {
        return ImmutableList.copyOf(this.modules);
    }
}
