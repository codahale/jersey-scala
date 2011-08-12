package com.codahale.jersey.inject.specs

import com.codahale.simplespec.Spec
import com.codahale.simplespec.annotation.test
import com.codahale.jersey.inject.ScalaCollectionQueryParamInjectable
import com.sun.jersey.server.impl.model.parameter.multivalued.MultivaluedParameterExtractor
import com.sun.jersey.api.core.{ExtendedUriInfo, HttpContext}
import javax.ws.rs.core.MultivaluedMap
import org.specs2.mock.Mockito

class ScalaCollectionQueryParamInjectableSpec extends Spec with Mockito {
  // TODO: Aug 17, 2010 <coda> -- test error handling

  val extractor = mock[MultivaluedParameterExtractor]
  val context = mock[HttpContext]
  val uriInfo = mock[ExtendedUriInfo]
  val params = mock[MultivaluedMap[String, String]]
  val extracted = mock[Object]

  extractor.extract(params) returns extracted
  context.getUriInfo returns uriInfo

  class `A Scala collection query param injectable with decoding` {
    val injectable = new ScalaCollectionQueryParamInjectable(extractor, true)
    uriInfo.getQueryParameters(true) returns params

    @test def `extracts the query parameters` = {
      val e = injectable.getValue(context)

      e must beEqualTo(extracted)
    }
  }

  class `A Scala collection query param injectable without decoding` {
    val injectable = new ScalaCollectionQueryParamInjectable(extractor, false)
    uriInfo.getQueryParameters(false) returns params

    @test def `extracts the query parameters` = {
      val e = injectable.getValue(context)

      e must beEqualTo(extracted)
    }
  }
}
