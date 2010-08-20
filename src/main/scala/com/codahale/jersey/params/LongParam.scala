package com.codahale.jersey.params

/**
 * Parses longs.
 *
 * @author coda
 */
case class LongParam(s: String) extends AbstractParam[Long](s) {
  protected def parse(input: String) = input.toLong

  override protected def renderError(input: String, e: Throwable) =
    "Invalid parameter: %s (Must be an integer value.)".format(input)
}
