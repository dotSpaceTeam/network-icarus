package dev.dotspace.network.library.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;


/**
 * Service to read content from file and map to content class.
 */
public interface IConfigService {
  /**
   * Read content of a {@link IConfigFile} instance from file.
   *
   * @param typeClass class of {@link IConfigFile} to map file content to.
   * @param file      name and path of file.
   * @param <TYPE>    generic type of mapped object and config.
   * @return config wrapped in optional for better code quality.
   * @throws IOException          if given file is not present in system or json parse fails.
   * @throws NullPointerException if one parameter is null.
   */
  <TYPE extends IConfigFile> @NotNull Optional<TYPE> readFile(@Nullable final Class<TYPE> typeClass,
                                                              @Nullable final String file) throws IOException;

  /**
   * Read content of a {@link IConfigFile} instance from resource.
   *
   * @param typeClass class of {@link IConfigFile} to map resource content to.
   * @param file      name and path of file.
   * @param <TYPE>    generic type of mapped object and config.
   * @return config wrapped in optional for better code quality.
   * @throws IOException          if given file is not present in system or json parse fails.
   * @throws NullPointerException if one parameter is null.
   */
  <TYPE extends IConfigFile> @NotNull Optional<TYPE> readResource(@Nullable final Class<?> loaderClass,
                                                                  @Nullable final Class<TYPE> typeClass,
                                                                  @Nullable final String file) throws IOException;

}
