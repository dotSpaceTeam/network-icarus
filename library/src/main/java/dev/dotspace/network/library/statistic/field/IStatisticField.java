package dev.dotspace.network.library.statistic.field;

import org.jetbrains.annotations.NotNull;

public interface IStatisticField<TYPE> {

  @NotNull String fieldName();

  @NotNull TYPE fieldType();

}
