package com.codahale.jersey.providers.specs

import com.codahale.simplespec.Spec
import org.specs.mock.Mockito
import com.codahale.jersey.providers.JValueProvider
import javax.ws.rs.core.MediaType
import net.liftweb.json.JsonAST.{JObject, JValue, JField, JInt}
import javax.ws.rs.WebApplicationException
import java.io.{ByteArrayOutputStream, ByteArrayInputStream}

object JValueProviderSpec extends Spec with Mockito {
  class `A JValue instance` {
    val value = mock[JValue]
    val provider = new JValueProvider

    def `should be writable` {
      provider.isWriteable(value.getClass, null, null, null) must beTrue
    }

    def `should be readable` {
      provider.isWriteable(value.getClass, null, null, null) must beTrue
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
          response.getEntity must beEqualTo("expected field or array\nNear: {\"yay\": 1")
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

  class `Rendering an application/json+pretty response entity` {
    val provider = new JValueProvider
    val json = JObject(List(JField("yay", JInt(1))))

    def `should produce a pretty JSON object` {
      val output = new ByteArrayOutputStream
      provider.writeTo(json, null, null, null, new MediaType("application", "json+pretty"), null, output)

      output.toString must beEqualTo("{\n  \"yay\":1\n}")
    }
  }
}
