package dev.dotspace.network.node.benchmark;

import dev.dotspace.common.function.ThrowableRunnable;
import org.jetbrains.annotations.Nullable;


public record ImmutableBenchmark(@Nullable String caseName,
                                 @Nullable ThrowableRunnable caseRunnable) implements IBenchmark {
}
