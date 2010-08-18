package com.codahale.jersey

import scala.collection.JavaConversions.asIterable
import javax.ws.rs.core.MultivaluedMap
import com.sun.jersey.server.impl.model.parameter.multivalued.MultivaluedParameterExtractor

/**
 * Given a parameter name, a possibly-null default value, and a fresh collection
 * builder, attempts to extract all the parameter values and return a collection
 * instance. If defaultValue is null and no parameter exists, returns an empty
 * collection.
 *
 * @author coda
 */
class ScalaCollectionParameterExtractor(val parameter: String,
                                        val defaultValue: String,
                                        val collectionBuilder: CollectionBuilder[_ <: Object])
        extends MultivaluedParameterExtractor {

  def getName = parameter
  def getDefaultStringValue = defaultValue
  def extract(parameters: MultivaluedMap[String, String]) = {
    val builder = collectionBuilder()
    val params = parameters.get(parameter)
    if (params != null) {
      builder.sizeHint(params.size)
      builder ++= asIterable(params).toTraversable
    } else if (defaultValue != null) {
      builder += defaultValue
    }
    builder.result()
  }
}
