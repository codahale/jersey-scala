package com.codahale.jersey.providers.specs

import com.codahale.simplespec.Spec
import org.junit.Test
import javax.ws.rs.core.MediaType
import com.codahale.jersey.providers.ArrayProvider
import javax.ws.rs.WebApplicationException
import java.io.{ByteArrayOutputStream, ByteArrayInputStream}

class ArrayProviderSpec extends Spec {
  private val provider = new ArrayProvider[Int]

  class `An array of ints` {
    @Test def `is writable` = {
      provider.isWriteable(Array.empty[Int].getClass, null, null, MediaType.APPLICATION_JSON_TYPE).must(be(true))
    }

    @Test def `is readable` = {
      provider.isReadable(Array.empty[Int].getClass, null, null, MediaType.APPLICATION_JSON_TYPE).must(be(true))
    }
  }

  class `Parsing an application/json request entity` {
    val entity = new ByteArrayInputStream("[1, 2, 3]".getBytes)

    @Test def `returns an array of the given type` = {
      provider.readFrom(classOf[Array[Int]], null, null, null, null, entity).must(be(Array(1, 2, 3)))
    }
  }

  class `Parsing an malformed application/json request entity` {
    val entity = new ByteArrayInputStream("[1, 2".getBytes)

    @Test def `throws a 400 Bad Request WebApplicationException` = {
      evaluating {
        provider.readFrom(classOf[Array[Int]], null, null, null, null, entity)
      }.must(throwAnExceptionLike {
        case e: WebApplicationException => {
          val response = e.getResponse
          response.getStatus.must(be(400))
          response.getEntity.must(be("Malformed JSON. Unexpected end-of-input: expected close marker for ARRAY at character offset 14."))
        }
      })
    }
  }

  class `Rendering an application/json response entity` {
    @Test def `produces a compact JSON array` = {
      val output = new ByteArrayOutputStream
      provider.writeTo(Array(1, 2, 3), null, null, null, MediaType.APPLICATION_JSON_TYPE, null, output)

      output.toString.must(be("[1,2,3]"))
    }
  }
}
