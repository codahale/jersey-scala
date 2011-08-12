package com.codahale.jersey.providers.specs

import com.codahale.simplespec.Spec
import javax.ws.rs.core.MediaType
import com.codahale.jersey.providers.JsonCaseClassProvider
import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import javax.ws.rs.WebApplicationException
import com.codahale.simplespec.annotation.test

case class Role(name: String)
case class Person(name: String, age: Int, roles: List[Role])

class JsonCaseClassProviderSpec extends Spec {
  val provider = new JsonCaseClassProvider
  val entity = """{"name":"Coda","age":29,"roles":[{"name":"badass"},{"name":"beardo"}]}"""
  val coda = Person("Coda", 29, List(Role("badass"), Role("beardo")))

  class `A case class instance` {
    @test def `is writable` = {
      provider.isWriteable(coda.getClass, null, null, MediaType.APPLICATION_JSON_TYPE) must beTrue
    }

    @test def `is readable` = {
      provider.isReadable(coda.getClass, null, null, MediaType.APPLICATION_JSON_TYPE) must beTrue
    }
  }

  class `Parsing an application/json request entity` {
    @test def `returns a case class instance` = {
      val value = provider.readFrom(classOf[Person].asInstanceOf[Class[Product]], null, null, null, null, new ByteArrayInputStream(entity.getBytes))

      value must beEqualTo(coda)
    }
  }

  class `Parsing an malformed application/json request entity` {
    val entity = "{\"yay\": 1"
    val provider = new JsonCaseClassProvider

    @test def `throws a 400 Bad Request WebApplicationException` = {
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
    val entity = "{\"yay\": 1}"
    val provider = new JsonCaseClassProvider

    @test def `throws a 400 Bad Request WebApplicationException` = {
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
    @test def `produces a compact JSON object` = {
      val output = new ByteArrayOutputStream
      provider.writeTo(coda, null, null, null, MediaType.APPLICATION_JSON_TYPE, null, output)

      output.toString must beEqualTo(entity)
    }
  }
}
