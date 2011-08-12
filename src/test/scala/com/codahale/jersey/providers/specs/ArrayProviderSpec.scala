package com.codahale.jersey.providers.specs

import com.codahale.simplespec.Spec
import com.codahale.simplespec.annotation.test
import javax.ws.rs.core.MediaType
import com.codahale.jersey.providers.ArrayProvider
import javax.ws.rs.WebApplicationException
import java.io.{ByteArrayOutputStream, ByteArrayInputStream}

class ArrayProviderSpec extends Spec {
  private val provider = new ArrayProvider[Int]

  class `An array of ints` {
    @test def `is writable` = {
      provider.isWriteable(Array.empty[Int].getClass, null, null, MediaType.APPLICATION_JSON_TYPE) must beTrue
    }

    @test def `is readable` = {
      provider.isReadable(Array.empty[Int].getClass, null, null, MediaType.APPLICATION_JSON_TYPE) must beTrue
    }
  }

  class `Parsing an application/json request entity` {
    val entity = new ByteArrayInputStream("[1, 2, 3]".getBytes)

    @test def `returns an array of the given type` = {
      provider.readFrom(classOf[Array[Int]], null, null, null, null, entity).toList must beEqualTo(List(1, 2, 3))
    }
  }

  class `Parsing an malformed application/json request entity` {
    val entity = new ByteArrayInputStream("[1, 2".getBytes)

    @test def `throws a 400 Bad Request WebApplicationException` = {
      provider.readFrom(classOf[Array[Int]], null, null, null, null, entity) must throwA[WebApplicationException].like {
        case e: WebApplicationException => {
          val response = e.getResponse
          response.getStatus must beEqualTo(400)
          response.getEntity must beEqualTo("Malformed JSON. Unexpected end-of-input: expected close marker for ARRAY at character offset 14.")
        }
      }
    }
  }

  class `Rendering an application/json response entity` {
    @test def `produces a compact JSON array` = {
      val output = new ByteArrayOutputStream
      provider.writeTo(Array(1, 2, 3), null, null, null, MediaType.APPLICATION_JSON_TYPE, null, output)

      output.toString must beEqualTo("[1,2,3]")
    }
  }
}
