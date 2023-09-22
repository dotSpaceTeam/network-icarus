package dev.dotspace.network.library.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.inject.Singleton;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;


@Log4j2
@Singleton
public final class ConfigService implements IConfigService {
  /**
   * Default jackson mapper for all services.
   */
  private final static ObjectMapper MAPPER = new ObjectMapper();

  /**
   * See {@link IConfigService#readFile(Class, String)}.
   */
  @Override
  public @NotNull <TYPE extends IConfigFile> Optional<TYPE> readFile(@Nullable Class<TYPE> typeClass,
                                                                     @Nullable String file) throws IOException {
    //Null check
    Objects.requireNonNull(typeClass);
    Objects.requireNonNull(file);


    return Optional.ofNullable(MAPPER.readValue(new File(file), typeClass));
  }

  /**
   * See {@link IConfigService#readResource(Class, Class, String)}.
   */
  @Override
  public @NotNull <TYPE extends IConfigFile> Optional<TYPE> readResource(@Nullable Class<?> loaderClass,
                                                                         @Nullable Class<TYPE> typeClass,
                                                                         @Nullable String file) throws IOException {
    //Null check
    Objects.requireNonNull(loaderClass);
    Objects.requireNonNull(typeClass);
    Objects.requireNonNull(file);

    //Start
    log.debug("Reading resource file={} loader={}.", file, loaderClass.getName());

    //Read input stream for file.
    try (final InputStream stream = loaderClass.getClassLoader().getResourceAsStream(file)) {
      //If stream is null, no file to be read.
      if (stream == null) {
        log.warn("Resource file={} not present!", file);
        return Optional.empty();
      }
      log.info("Reading file content of file={}.", file);
      //Present file, read content.
      final TYPE type = MAPPER.readValue(stream, typeClass);
      log.info("Successfully read file={}.", file);

      //Return content.
      return Optional.ofNullable(type);
    }
  }

}
