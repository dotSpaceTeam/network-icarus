package dev.dotspace.network.library.connection;

import dev.dotspace.network.library.system.IParticipant;
import dev.dotspace.network.library.time.ITimestamp;
import org.jetbrains.annotations.NotNull;


public interface IRestRequest extends IAddressName, ITimestamp {
  /**
   * Path of request.
   *
   * @return path done for request.
   */
  @NotNull String path();

  /**
   * Client send this request. Or empty if no client id is not present.
   *
   * @return client id send this request.
   */
  @NotNull IParticipant client();

  /**
   * Method of this request. [GET, PUT, POST, ...]
   *
   * @return name of method.
   */
  @NotNull String method();  /*
   *
   * Note for request.
   *
   * @return note if specified, else empty.
   */

  @NotNull String note();

  /**
   * Time the request took to process.
   *
   * @return time in ms.
   */
  long processTime();

  /**
   * Status request.
   * 200 -> ok
   * 404 -> not found
   *
   * @return status code.
   */
  int status();
}
