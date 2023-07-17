package dev.dotspace.network.library.state;

import org.jetbrains.annotations.NotNull;

public record ImmutableBooleanState(Boolean state,
                                    @NotNull String message) implements IState<Boolean> {
}
