package com.codahale.jersey.providers.specs

import com.codahale.simplespec.Spec
import com.codahale.jersey.providers.JValueProvider
import javax.ws.rs.core.MediaType
import com.codahale.jerkson.AST._
import javax.ws.rs.WebApplicationException
import java.io.{ByteArrayOutputStream, ByteArrayInputStream}
import org.junit.Test

class JValueProviderSpec extends Spec {
  class `A JValue instance` {
    val value = mock[JValue]
    val provider = new JValueProvider

    @Test def `is writable` = {
      provider.isWriteable(value.getClass, null, null, null).must(be(true))
    }

    @Test def `is readable` = {
      provider.isReadable(value.getClass, null, null, null).must(be(true))
    }
  }

  class `Parsing an application/json request entity` {
    val entity = "{\"yay\": 1}"
    val provider = new JValueProvider

    @Test def `returns a JValue instance` = {
      val value = provider.readFrom(null, null, null, null, null, new ByteArrayInputStream(entity.getBytes))

      value.must(be(JObject(List(JField("yay", JInt(1))))))
    }
  }

  class `Parsing an invalid application/json request entity` {
    val entity = "{\"yay\": 1"
    val provider = new JValueProvider

    @Test def `throws a 400 Bad Request WebApplicationException` = {
      evaluating {
        provider.readFrom(null, null, null, null, null, new ByteArrayInputStream(entity.getBytes))
      }.must(throwAnExceptionLike {
        case e: WebApplicationException => {
          val response = e.getResponse
          response.getStatus.must(be(400))
          response.getEntity.must(be("Malformed JSON. Unexpected end-of-input: expected close marker for OBJECT at character offset 26."))
        }
      })
    }
  }

  class `Rendering an application/json response entity` {
    val provider = new JValueProvider
    val json = JObject(List(JField("yay", JInt(1))))

    @Test def `produces a compact JSON object` = {
      val output = new ByteArrayOutputStream
      provider.writeTo(json, null, null, null, MediaType.APPLICATION_JSON_TYPE, null, output)
      output.toString.must(be("{\"yay\":1}"))
    }
  }
}
