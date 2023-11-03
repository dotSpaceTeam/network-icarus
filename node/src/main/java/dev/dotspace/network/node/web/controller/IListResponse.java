package dev.dotspace.network.node.web.controller;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;


/**
 * Response from server
 *
 * @param <TYPE> generic type of object
 */
public interface IListResponse<TYPE> extends IBodyResponse<List<TYPE>> {
  /**
   * List is a fragment.
   *
   * @return true, if part of a fragment.
   */
  //Swagger
  @Schema(example="false", description="If list a fragment of another list.")
  boolean fragmented();

  /**
   * List was already sorted.
   *
   * @return true, if list was already sorted
   */
  //Swagger
  @Schema(example="false", description="If list was sorted.")
  boolean sorted();
}