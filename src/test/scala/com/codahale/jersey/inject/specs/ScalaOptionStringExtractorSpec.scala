package com.codahale.jersey.inject.specs

import com.codahale.simplespec.Spec
import com.sun.jersey.core.util.MultivaluedMapImpl
import com.codahale.jersey.inject.ScalaOptionStringExtractor

object ScalaOptionStringExtractorSpec extends Spec {
  class `Extracting a parameter` {
    private val extractor = new ScalaOptionStringExtractor("name", "default")

    def `should have a name` = {
      extractor.getName must beEqualTo("name")
    }

    def `should have a default value` = {
      extractor.getDefaultStringValue must beEqualTo("default")
    }

    def `should extract the first of a set of parameter values` = {
      val params = new MultivaluedMapImpl()
      params.add("name", "one")
      params.add("name", "two")
      params.add("name", "three")

      val result = extractor.extract(params).asInstanceOf[Option[String]]
      result must beEqualTo(Some("one"))
    }

    def `should use the default value if no parameter exists` = {
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params).asInstanceOf[Option[String]]
      result must beEqualTo(Some("default"))
    }
  }

  class `Extracting a parameter with no default value` {
    private val extractor = new ScalaOptionStringExtractor("name", null)

    def `should return None` = {
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params).asInstanceOf[Option[String]]
      result must beEqualTo(None)
    }
  }
}
