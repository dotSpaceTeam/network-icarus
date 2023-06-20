package dev.dotspace.network.node.message.text.element;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

@Component("elementManager")
public final class ElementManager {
  // private final @NotNull Map<String, Class<? extends AbstractElement>> elementMap;

  public ElementManager() {
    //this.elementMap = new HashMap<>();
    this.registerElements(this.getClass().getPackageName());
  }

  public void registerElements(@Nullable final String path) {
    //Null check
    Objects.requireNonNull(path);

    new Reflections(path).getTypesAnnotatedWith(TextElement.class).forEach(aClass -> {
      final TextElement element = aClass.getAnnotation(TextElement.class);

      //this.elementMap.put(element.name().toUpperCase(),
      // new ElementInfo(aClass, ReflectionHelper.field(aClass, "")));
      //System.out.println(this.elementMap.get(element.name().toUpperCase()));
      // System.out.println(element.name());
    });
  }

  @SuppressWarnings("all")
  public void element(@Nullable final String name,
                      @Nullable final String value,
                      @Nullable final String option) {

  /*  return (TYPE) Optional
      .ofNullable(this.elementMap.get(name.toUpperCase()))
      .map(elementInfo -> {
        try {
          return elementInfo.getConstructor(String.class, String.class).newInstance(value, option);
        } catch (final Exception exception) {
          exception.printStackTrace();
          throw new NullPointerException();
        }
      })
      .orElse(null);

   */
  }

  private record ElementInfo(@NotNull Class<?> elementClass,
                             @Nullable Field elementName) {

  }
}
