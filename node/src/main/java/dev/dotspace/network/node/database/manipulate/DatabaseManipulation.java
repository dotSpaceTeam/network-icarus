package dev.dotspace.network.node.database.manipulate;

/**
 * Type of database access.
 */
public enum DatabaseManipulation {
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
