package dev.dotspace.network.node.message.text.element;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Accessors(fluent = true)
@NoArgsConstructor
public abstract class AbstractElement {
  @ElementName
  @Getter
  private @Nullable String elementName;

  @ElementValue
  @Getter
  private @Nullable String elementValue;

  @ElementOption
  @Getter
  private @Nullable String elementOption;

  @Override
  public String toString() {
    return "AbstractElement{" +
      "elementName='" + elementName + '\'' +
      ", elementValue='" + elementValue + '\'' +
      ", elementOption='" + elementOption + '\'' +
      '}';
  }
}
