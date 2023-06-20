package dev.dotspace.network.library.statistic.board;

import dev.dotspace.network.library.statistic.field.IStatisticField;
import dev.dotspace.network.library.statistic.value.IStatisticValue;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface IStatisticBoard<OWNER> {

  @NotNull Map<IStatisticField<?>, IStatisticValue> values();

}
