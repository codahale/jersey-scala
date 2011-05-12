package com.codahale.jersey.inject.specs

import com.codahale.simplespec.Spec
import com.codahale.jersey.inject.ScalaCollectionQueryParamInjectable
import com.sun.jersey.server.impl.model.parameter.multivalued.MultivaluedParameterExtractor
import com.sun.jersey.api.core.{ExtendedUriInfo, HttpContext}
import javax.ws.rs.core.MultivaluedMap
import org.specs2.mock.Mockito

object ScalaCollectionQueryParamInjectableSpec extends Spec with Mockito {
  // TODO: Aug 17, 2010 <coda> -- test error handling

  private val extractor = mock[MultivaluedParameterExtractor]
  private val context = mock[HttpContext]
  private val uriInfo = mock[ExtendedUriInfo]
  private val params = mock[MultivaluedMap[String, String]]
  private val extracted = mock[Object]

  extractor.extract(params) returns extracted
  context.getUriInfo returns uriInfo

  class `A Scala collection query param injectable with decoding` {
    private val injectable = new ScalaCollectionQueryParamInjectable(extractor, true)
    uriInfo.getQueryParameters(true) returns params

    def `should extract the query parameters` = {
      val e = injectable.getValue(context)

      e must beEqualTo(extracted)
    }
  }

  class `A Scala collection query param injectable without decoding` {
    private val injectable = new ScalaCollectionQueryParamInjectable(extractor, false)
    uriInfo.getQueryParameters(false) returns params

    def `should extract the query parameters` = {
      val e = injectable.getValue(context)

      e must beEqualTo(extracted)
    }
  }
}
