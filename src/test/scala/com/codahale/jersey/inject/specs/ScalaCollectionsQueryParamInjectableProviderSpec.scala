package com.codahale.jersey.inject.specs

import com.codahale.simplespec.Spec
import javax.ws.rs.QueryParam
import com.sun.jersey.api.model.Parameter
import com.sun.jersey.core.spi.component.{ComponentContext, ComponentScope}
import com.codahale.jersey.inject.{ScalaCollectionQueryParamInjectable, ScalaCollectionsQueryParamInjectableProvider}
import com.sun.jersey.core.util.MultivaluedMapImpl
import com.sun.jersey.api.core.{HttpContext, ExtendedUriInfo}
import org.specs2.mock.Mockito

object ScalaCollectionsQueryParamInjectableProviderSpec extends Spec with Mockito {
  class `A Scala collections query param injectable provider` {
    private val httpContext = mock[HttpContext]
    private val uriInfo = mock[ExtendedUriInfo]
    private val params = new MultivaluedMapImpl()
    params.add("name", "one")
    params.add("name", "two")
    params.add("name", "three")

    httpContext.getUriInfo returns uriInfo
    uriInfo.getQueryParameters(any[Boolean]) returns params

    private val context = mock[ComponentContext]
    private val queryParam = mock[QueryParam]

    private val provider = new ScalaCollectionsQueryParamInjectableProvider


    def `should have a per-request scope` = {
      provider.getScope must beEqualTo(ComponentScope.PerRequest)
    }

    def `should return an injectable for Seq instances` = {
      val param = new Parameter(Array(), null, null, "name", null, classOf[Seq[String]], false, "default")
      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]

      injectable.getValue(httpContext) must beEqualTo(Seq("one", "two", "three"))
    }

    def `should return an injectable for List instances` = {
      val param = new Parameter(Array(), null, null, "name", null, classOf[List[String]], false, "default")
      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]

      injectable.getValue(httpContext) must beEqualTo(List("one", "two", "three"))
    }

    def `should return an injectable for Vector instances` = {
      val param = new Parameter(Array(), null, null, "name", null, classOf[Vector[String]], false, "default")
      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]

      injectable.getValue(httpContext) must beEqualTo(Vector("one", "two", "three"))
    }

    def `should return an injectable for IndexedSeq instances` = {
      val param = new Parameter(Array(), null, null, "name", null, classOf[IndexedSeq[String]], false, "default")
      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]

      injectable.getValue(httpContext) must beEqualTo(IndexedSeq("one", "two", "three"))
    }

    def `should return an injectable for Set instances` = {
      val param = new Parameter(Array(), null, null, "name", null, classOf[Set[String]], false, "default")
      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]

      injectable.getValue(httpContext) must beEqualTo(Set("one", "two", "three"))
    }

    def `should return an injectable for Option instances` = {
      val param = new Parameter(Array(), null, null, "name", null, classOf[Option[String]], false, "default")
      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]

      injectable.getValue(httpContext) must beEqualTo(Some("one"))
    }
  }
}
