package com.codahale.jersey.inject.specs

import com.codahale.simplespec.Spec
import org.junit.Test
import com.sun.jersey.api.model.Parameter
import com.sun.jersey.core.spi.component.{ComponentContext, ComponentScope}
import com.sun.jersey.core.util.MultivaluedMapImpl
import javax.ws.rs.FormParam
import com.codahale.jersey.inject.{ScalaCollectionsFormParamInjectableProvider, ScalaCollectionFormParamInjectable}
import com.sun.jersey.api.core.{HttpRequestContext, HttpContext}
import com.sun.jersey.api.representation.Form

class ScalaCollectionsFormParamInjectableProviderSpec extends Spec {
  class `A Scala collections form param injectable provider` {
    val httpContext = mock[HttpContext]
    val requestContext = mock[HttpRequestContext]

    val params = new Form()
    params.add("name", "one")
    params.add("name", "two")
    params.add("name", "three")

    httpContext.getRequest returns requestContext
    requestContext.getFormParameters returns params

    val context = mock[ComponentContext]
    val formParam = mock[FormParam]

    val provider = new ScalaCollectionsFormParamInjectableProvider


    @Test def `has a per-request scope` = {
      provider.getScope.must(be(ComponentScope.PerRequest))
    }

    @Test def `returns an injectable for Seq instances` = {
      val param = new Parameter(Array(), null, null, "name", null, classOf[Seq[String]], false, "default")
      val injectable = provider.getInjectable(context, formParam, param).asInstanceOf[ScalaCollectionFormParamInjectable]

      injectable.getValue(httpContext).must(be(Seq("one", "two", "three")))
    }

    @Test def `returns an injectable for List instances` = {
      val param = new Parameter(Array(), null, null, "name", null, classOf[List[String]], false, "default")
      val injectable = provider.getInjectable(context, formParam, param).asInstanceOf[ScalaCollectionFormParamInjectable]

      injectable.getValue(httpContext).must(be(List("one", "two", "three")))
    }

    @Test def `returns an injectable for Vector instances` = {
      val param = new Parameter(Array(), null, null, "name", null, classOf[Vector[String]], false, "default")
      val injectable = provider.getInjectable(context, formParam, param).asInstanceOf[ScalaCollectionFormParamInjectable]

      injectable.getValue(httpContext).must(be(Vector("one", "two", "three")))
    }

    @Test def `returns an injectable for IndexedSeq instances` = {
      val param = new Parameter(Array(), null, null, "name", null, classOf[IndexedSeq[String]], false, "default")
      val injectable = provider.getInjectable(context, formParam, param).asInstanceOf[ScalaCollectionFormParamInjectable]

      injectable.getValue(httpContext).must(be(IndexedSeq("one", "two", "three")))
    }

    @Test def `return an injectable for Set instances` = {
      val param = new Parameter(Array(), null, null, "name", null, classOf[Set[String]], false, "default")
      val injectable = provider.getInjectable(context, formParam, param).asInstanceOf[ScalaCollectionFormParamInjectable]

      injectable.getValue(httpContext).must(be(Set("one", "two", "three")))
    }

    @Test def `returns an injectable for Option instances` = {
      val param = new Parameter(Array(), null, null, "name", null, classOf[Option[String]], false, "default")
      val injectable = provider.getInjectable(context, formParam, param).asInstanceOf[ScalaCollectionFormParamInjectable]

      injectable.getValue(httpContext).must(be(Some("one")))
    }
  }
}
