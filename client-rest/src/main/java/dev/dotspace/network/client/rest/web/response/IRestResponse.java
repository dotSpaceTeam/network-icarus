package dev.dotspace.network.client.rest.web.response;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;


public interface IRestResponse<TYPE> {
  /**
   * Content of response body.
   *
   * @return
   */
  //Todo
  @NotNull TYPE body() throws NullPointerException;

  /**
   * State of response. If successfully {@link ResponseState#SUCCESS}
   *
   * @return
   */
  @NotNull ResponseState state();

  /**
   * Time request took.
   *
   * @return
   */
  long processTime();

}
