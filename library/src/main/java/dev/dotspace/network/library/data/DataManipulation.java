package dev.dotspace.network.library.data;

/**
 * Type of database access.
 */
public enum DataManipulation {
  /**
   * Data was updated.
   */
  UPDATE,

  /**
   * Data was created.
   */
  CREATE,

  /**
   * Data was deleted.
   */
  DELETE
}
