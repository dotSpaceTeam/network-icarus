package dev.dotspace.network.library.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent=true)
public final class EditiableObject<TYPE> {
  private @Nullable TYPE type;
}
