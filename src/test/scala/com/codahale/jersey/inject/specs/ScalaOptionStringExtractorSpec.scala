package com.codahale.jersey.inject.specs

import com.codahale.simplespec.Spec
import org.junit.Test
import com.sun.jersey.core.util.MultivaluedMapImpl
import com.codahale.jersey.inject.ScalaOptionStringExtractor

class ScalaOptionStringExtractorSpec extends Spec {
  class `Extracting a parameter` {
    val extractor = new ScalaOptionStringExtractor("name", "default")

    @Test def `has a name` = {
      extractor.getName.must(be("name"))
    }

    @Test def `has a default value` = {
      extractor.getDefaultStringValue.must(be("default"))
    }

    @Test def `extracts the first of a set of parameter values` = {
      val params = new MultivaluedMapImpl()
      params.add("name", "one")
      params.add("name", "two")
      params.add("name", "three")

      val result = extractor.extract(params).asInstanceOf[Option[String]]
      result.must(be(Some("one")))
    }

    @Test def `uses the default value if no parameter exists` = {
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params).asInstanceOf[Option[String]]
      result.must(be(Some("default")))
    }
  }

  class `Extracting a parameter with no default value` {
    val extractor = new ScalaOptionStringExtractor("name", null)

    @Test def `returns None` = {
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params).asInstanceOf[Option[String]]
      result.must(be(None))
    }
  }
}
