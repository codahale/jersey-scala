package com.codahale.jersey.providers.specs

import com.codahale.simplespec.Spec
import org.specs.mock.Mockito
import com.codahale.jersey.providers.JValueProvider
import javax.ws.rs.core.MediaType
import com.codahale.jerkson.AST._
import javax.ws.rs.WebApplicationException
import java.io.{ByteArrayOutputStream, ByteArrayInputStream}

object JValueProviderSpec extends Spec with Mockito {
  detailedDiffs("()")

  class `A JValue instance` {
    val value = mock[JValue]
    val provider = new JValueProvider

    def `should be writable` {
      provider.isWriteable(value.getClass, null, null, null) must beTrue
    }

    def `should be readable` {
      provider.isReadable(value.getClass, null, null, null) must beTrue
    }
  }

  class `Parsing an application/json request entity` {
    val entity = "{\"yay\": 1}"
    val provider = new JValueProvider

    def `should return a JValue instance` {
      val value = provider.readFrom(null, null, null, null, null, new ByteArrayInputStream(entity.getBytes))

      value must beEqualTo(JObject(List(JField("yay", JInt(1)))))
    }
  }

  class `Parsing an invalid application/json request entity` {
    val entity = "{\"yay\": 1"
    val provider = new JValueProvider

    def `should throw a 400 Bad Request WebApplicationException` {
      provider.readFrom(null, null, null, null, null, new ByteArrayInputStream(entity.getBytes)) must throwA[WebApplicationException].like {
        case e: WebApplicationException =>
          val response = e.getResponse
          response.getStatus must beEqualTo(400)
          response.getEntity must beEqualTo("Malformed JSON. Unexpected end-of-input: expected close marker for OBJECT at character offset 26.")
      }
    }
  }

  class `Rendering an application/json response entity` {
    val provider = new JValueProvider
    val json = JObject(List(JField("yay", JInt(1))))

    def `should produce a compact JSON object` {
      val output = new ByteArrayOutputStream
      provider.writeTo(json, null, null, null, MediaType.APPLICATION_JSON_TYPE, null, output)
      output.toString must beEqualTo("{\"yay\":1}")
    }
  }
}
