package com.codahale.jersey.inject.specs

import com.codahale.simplespec.Spec
import org.junit.Test
import com.codahale.jersey.inject.ScalaCollectionStringReaderExtractor
import com.sun.jersey.core.util.MultivaluedMapImpl

class ScalaCollectionStringReaderExtractorSpec extends Spec {
  class `Extracting a parameter` {
    val extractor = new ScalaCollectionStringReaderExtractor[Set]("name", "default", Set)

    @Test def `has a name` = {
      extractor.getName.must(be("name"))
    }

    @Test def `has a default value` = {
      extractor.getDefaultStringValue.must(be("default"))
    }

    @Test def `extracts a set of parameter values` = {
      val params = new MultivaluedMapImpl()
      params.add("name", "one")
      params.add("name", "two")
      params.add("name", "three")

      val result = extractor.extract(params).asInstanceOf[Set[String]]
      result.must(be(Set("one", "two", "three")))
    }

    @Test def `uses the default value if no parameter exists` = {
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params).asInstanceOf[Set[String]]
      result.must(be(Set("default")))
    }
  }

  class `Extracting a parameter with no default value` {
    val extractor = new ScalaCollectionStringReaderExtractor[Set]("name", null, Set)

    @Test def `returns an empty collection` = {
      val params = new MultivaluedMapImpl()

      val result = extractor.extract(params).asInstanceOf[Set[String]]
      result.must(be(Set.empty[String]))
    }
  }
}
