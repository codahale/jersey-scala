package com.codahale.jersey.providers.specs

import com.codahale.simplespec.Spec
import javax.ws.rs.core.MediaType
import com.codahale.jersey.providers.JsonCaseClassProvider
import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import javax.ws.rs.WebApplicationException

case class Role(name: String)
case class Person(name: String, age: Int, roles: List[Role])

object JsonCaseClassProviderSpec extends Spec {
  private val provider = new JsonCaseClassProvider
  private val entity = """{"name":"Coda","age":29,"roles":[{"name":"badass"},{"name":"beardo"}]}"""
  private val coda = Person("Coda", 29, List(Role("badass"), Role("beardo")))

  class `A case class instance` {
    def `should be writable` = {
      provider.isWriteable(coda.getClass, null, null, MediaType.APPLICATION_JSON_TYPE) must beTrue
    }

    def `should be readable` = {
      provider.isReadable(coda.getClass, null, null, MediaType.APPLICATION_JSON_TYPE) must beTrue
    }
  }

  class `Parsing an application/json request entity` {
    def `should return a case class instance` = {
      val value = provider.readFrom(classOf[Person].asInstanceOf[Class[Product]], null, null, null, null, new ByteArrayInputStream(entity.getBytes))

      value must beEqualTo(coda)
    }
  }

  class `Parsing an malformed application/json request entity` {
    private val entity = "{\"yay\": 1"
    private val provider = new JsonCaseClassProvider

    def `should throw a 400 Bad Request WebApplicationException` = {
      provider.readFrom(classOf[Role].asInstanceOf[Class[Product]], null, null, null, null, new ByteArrayInputStream(entity.getBytes)) must throwA[WebApplicationException].like {
        case e: WebApplicationException => {
          val response = e.getResponse
          response.getStatus must beEqualTo(400)
          response.getEntity must beEqualTo("Malformed JSON. Unexpected end-of-input: expected close marker for OBJECT at character offset 26.")
        }
      }
    }
  }

  class `Parsing an invalid application/json request entity` {
    private val entity = "{\"yay\": 1}"
    private val provider = new JsonCaseClassProvider

    def `should throw a 400 Bad Request WebApplicationException` = {
      provider.readFrom(classOf[Role].asInstanceOf[Class[Product]], null, null, null, null, new ByteArrayInputStream(entity.getBytes)) must throwA[WebApplicationException].like {
        case e: WebApplicationException => {
          val response = e.getResponse
          response.getStatus must beEqualTo(400)
          response.getEntity must beEqualTo("Invalid JSON. Needed [name], but found [yay].")
        }
      }
    }
  }

  class `Rendering an application/json response entity` {
    def `should produce a compact JSON object` = {
      val output = new ByteArrayOutputStream
      provider.writeTo(coda, null, null, null, MediaType.APPLICATION_JSON_TYPE, null, output)

      output.toString must beEqualTo(entity)
    }
  }
}
