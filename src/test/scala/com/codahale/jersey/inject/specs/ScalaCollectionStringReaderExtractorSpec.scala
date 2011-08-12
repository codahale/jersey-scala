package com.codahale.jersey.inject.specs

import com.codahale.simplespec.Spec
import com.codahale.simplespec.annotation.test
import com.codahale.jersey.inject.ScalaCollectionStringReaderExtractor
import com.sun.jersey.core.util.MultivaluedMapImpl

class ScalaCollectionStringReaderExtractorSpec extends Spec {
  class `Extracting a parameter` {
    val extractor = new ScalaCollectionStringReaderExtractor[Set]("name", "default", Set)

    @test def `has a name` = {
      extractor.getName must beEqualTo("name")
    }

    @test def `has a default value` = {
      extractor.getDefaultStringValue must beEqualTo("default")
    }

    @test def `extracts a set of parameter values` = {
      val params = new MultivaluedMapImpl()
      params.add("name", "one")
      params.add("name", "two")
      params.add("name", "three")

      val result = extractor.extract(params).asInstanceOf[Set[String]]
      result must beEqualTo(Set("one", "two", "three"))
    }

    @test def `uses the default value if no parameter exists` = {
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params).asInstanceOf[Set[String]]
      result must beEqualTo(Set("default"))
    }
  }

  class `Extracting a parameter with no default value` {
    val extractor = new ScalaCollectionStringReaderExtractor[Set]("name", null, Set)

    @test def `returns an empty collection` = {
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params).asInstanceOf[Set[String]]
      result must beEqualTo(Set())
    }
  }
}
