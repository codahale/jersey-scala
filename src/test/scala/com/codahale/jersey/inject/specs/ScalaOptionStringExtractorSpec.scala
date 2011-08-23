package com.codahale.jersey.inject.specs

import com.codahale.simplespec.Spec
import com.codahale.simplespec.annotation.test
import com.sun.jersey.core.util.MultivaluedMapImpl
import com.codahale.jersey.inject.ScalaOptionStringExtractor

class ScalaOptionStringExtractorSpec extends Spec {
  class `Extracting a parameter` {
    val extractor = new ScalaOptionStringExtractor("name", "default")

    @test def `has a name` = {
      extractor.getName.must(be("name"))
    }

    @test def `has a default value` = {
      extractor.getDefaultStringValue.must(be("default"))
    }

    @test def `extracts the first of a set of parameter values` = {
      val params = new MultivaluedMapImpl()
      params.add("name", "one")
      params.add("name", "two")
      params.add("name", "three")

      val result = extractor.extract(params).asInstanceOf[Option[String]]
      result.must(be(Some("one")))
    }

    @test def `uses the default value if no parameter exists` = {
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params).asInstanceOf[Option[String]]
      result.must(be(Some("default")))
    }
  }

  class `Extracting a parameter with no default value` {
    val extractor = new ScalaOptionStringExtractor("name", null)

    @test def `returns None` = {
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params).asInstanceOf[Option[String]]
      result.must(be(None))
    }
  }
}
