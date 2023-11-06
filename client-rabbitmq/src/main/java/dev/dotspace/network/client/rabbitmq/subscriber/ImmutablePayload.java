package dev.dotspace.network.client.rabbitmq.subscriber;

import org.jetbrains.annotations.NotNull;

import java.util.Map;


public record ImmutablePayload<PAYLOAD>(@NotNull PAYLOAD payload,
                                        @NotNull Map<String, Object> headers
) implements IPayload<PAYLOAD> {
}
