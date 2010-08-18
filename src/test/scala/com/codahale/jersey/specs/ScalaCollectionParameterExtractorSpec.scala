package com.codahale.jersey.specs

import com.codahale.simplespec.Spec
import com.sun.jersey.core.util.MultivaluedMapImpl
import com.codahale.jersey.{SetCollectionBuilder, ScalaCollectionParameterExtractor}

object ScalaCollectionParameterExtractorSpec extends Spec {
  class `Extracting a query parameter` {
    val extractor = new ScalaCollectionParameterExtractor("name", "default", SetCollectionBuilder)

    def `should have a name` {
      extractor.getName must beEqualTo("name")
    }

    def `should have a default value` {
      extractor.getDefaultStringValue must beEqualTo("default")
    }

    def `should extract a set of parameter values` {
      val params = new MultivaluedMapImpl()
      params.add("name", "one")
      params.add("name", "two")
      params.add("name", "three")

      val result = extractor.extract(params).asInstanceOf[Set[String]]
      result must beEqualTo(Set("one", "two", "three"))
    }

    def `should use the default value if no parameter exists` {
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params).asInstanceOf[Set[String]]
      result must beEqualTo(Set("default"))
    }
  }

  class `Extracting a query parameter with no default value` {
    val extractor = new ScalaCollectionParameterExtractor("name", null, SetCollectionBuilder)

    def `should return an empty collection` {
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params).asInstanceOf[Set[String]]
      result must beEqualTo(Set())
    }
  }
}
