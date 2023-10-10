package dev.dotspace.network.node.benchmark;

import dev.dotspace.common.function.ThrowableRunnable;
import org.jetbrains.annotations.NotNull;


public interface IBenchmark {

  @NotNull String caseName();

  @NotNull ThrowableRunnable caseRunnable();

}
