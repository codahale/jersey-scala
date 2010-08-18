package com.codahale.jersey.specs

import com.codahale.simplespec.Spec
import org.specs.mock.Mockito
import javax.ws.rs.QueryParam
import com.sun.jersey.api.model.Parameter
import com.sun.jersey.core.spi.component.{ComponentContext, ComponentScope}
import com.codahale.jersey.{ScalaCollectionParameterExtractor, ScalaCollectionQueryParamInjectable, ScalaCollectionsQueryParamInjectableProvider}
import scala.collection.immutable.VectorBuilder
import scala.collection.mutable.AddingBuilder

object ScalaCollectionsQueryParamInjectableProviderSpec extends Spec with Mockito {
  class `A Scala collections query param injectable provider` {
    val context = mock[ComponentContext]
    val provider = new ScalaCollectionsQueryParamInjectableProvider
    val queryParam = mock[QueryParam]


    def `should have a per-request scope` {
      provider.getScope must beEqualTo(ComponentScope.PerRequest)
    }

    def `should decode the parameter if necessary` {
      val param = new Parameter(Array(), null, null, "name", null, classOf[Seq[String]], true, "default")
      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]

      injectable.decode must beFalse
    }

    def `should return an injectable for Seq instances` {
      val param = new Parameter(Array(), null, null, "name", null, classOf[Seq[String]], true, "default")
      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]

      val extractor = injectable.extractor.asInstanceOf[ScalaCollectionParameterExtractor[Seq[String]]]
      extractor.parameter must beEqualTo("name")
      extractor.defaultValue must beEqualTo("default")
      extractor.builder must beEqualTo(Seq.newBuilder[String])
    }

    def `should return an injectable for List instances` {
      val param = new Parameter(Array(), null, null, "name", null, classOf[List[String]], true, "default")
      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]

      val extractor = injectable.extractor.asInstanceOf[ScalaCollectionParameterExtractor[List[String]]]
      extractor.builder must beEqualTo(List.newBuilder[String])
    }

    def `should return an injectable for Vector instances` {
      val param = new Parameter(Array(), null, null, "name", null, classOf[Vector[String]], true, "default")
      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]

      val extractor = injectable.extractor.asInstanceOf[ScalaCollectionParameterExtractor[Vector[String]]]
      extractor.builder must haveClass[VectorBuilder[String]]
    }

    def `should return an injectable for IndexedSeq instances` {
      val param = new Parameter(Array(), null, null, "name", null, classOf[IndexedSeq[String]], true, "default")
      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]

      val extractor = injectable.extractor.asInstanceOf[ScalaCollectionParameterExtractor[IndexedSeq[String]]]
      extractor.builder must haveClass[VectorBuilder[String]]
    }

    def `should return an injectable for Set instances` {
      val param = new Parameter(Array(), null, null, "name", null, classOf[Set[String]], true, "default")
      val injectable = provider.getInjectable(context, queryParam, param).asInstanceOf[ScalaCollectionQueryParamInjectable]

      val extractor = injectable.extractor.asInstanceOf[ScalaCollectionParameterExtractor[Set[String]]]
      extractor.builder must haveClass[AddingBuilder[String, Set[String]]]
    }
  }
}
