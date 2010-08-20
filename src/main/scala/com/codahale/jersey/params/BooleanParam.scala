package com.codahale.jersey.params

/**
 * Parses "true" and "false" to Boolean values.
 *
 * @author coda
 */
case class BooleanParam(s: String) extends AbstractParam[Boolean](s) {
  protected def parse(input: String) = input.toBoolean

  override protected def renderError(input: String, e: Throwable) =
    "Invalid parameter: %s (Must be \"true\" or \"false\".)".format(input)
}
