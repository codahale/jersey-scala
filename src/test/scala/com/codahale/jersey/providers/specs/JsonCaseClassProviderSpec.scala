package com.codahale.jersey.providers.specs

import com.codahale.simplespec.Spec
import javax.ws.rs.core.MediaType
import com.codahale.jersey.providers.JsonCaseClassProvider
import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import javax.ws.rs.WebApplicationException
import org.junit.Test

case class Role(name: String)
case class Person(name: String, age: Int, roles: List[Role])

class JsonCaseClassProviderSpec extends Spec {
  val provider = new JsonCaseClassProvider
  val entity = """{"name":"Coda","age":29,"roles":[{"name":"badass"},{"name":"beardo"}]}"""
  val coda = Person("Coda", 29, List(Role("badass"), Role("beardo")))

  class `A case class instance` {
    @Test def `is writable` = {
      provider.isWriteable(coda.getClass, null, null, MediaType.APPLICATION_JSON_TYPE).must(be(true))
    }

    @Test def `is readable` = {
      provider.isReadable(coda.getClass, null, null, MediaType.APPLICATION_JSON_TYPE).must(be(true))
    }
  }

  class `Parsing an application/json request entity` {
    @Test def `returns a case class instance` = {
      val value = provider.readFrom(classOf[Person].asInstanceOf[Class[Product]], null, null, null, null, new ByteArrayInputStream(entity.getBytes))

      value.must(be(coda))
    }
  }

  class `Parsing an malformed application/json request entity` {
    val entity = "{\"yay\": 1"
    val provider = new JsonCaseClassProvider

    @Test def `throws a 400 Bad Request WebApplicationException` = {
      evaluating {
        provider.readFrom(classOf[Role].asInstanceOf[Class[Product]], null, null, null, null, new ByteArrayInputStream(entity.getBytes))
      }.must(throwAnExceptionLike {
        case e: WebApplicationException => {
          val response = e.getResponse
          response.getStatus.must(be(400))
          response.getEntity.must(be("Malformed JSON. Unexpected end-of-input: expected close marker for OBJECT at character offset 26."))
        }
      })
    }
  }

  class `Parsing an invalid application/json request entity` {
    val entity = "{\"yay\": 1}"
    val provider = new JsonCaseClassProvider

    @Test def `throws a 400 Bad Request WebApplicationException` = {
      evaluating {
        provider.readFrom(classOf[Role].asInstanceOf[Class[Product]], null, null, null, null, new ByteArrayInputStream(entity.getBytes))
      }.must(throwAnExceptionLike {
        case e: WebApplicationException => {
          val response = e.getResponse
          response.getStatus.must(be(400))
          response.getEntity.must(be("Invalid JSON. Needed [name], but found [yay]."))
        }
      })
    }
  }

  class `Rendering an application/json response entity` {
    @Test def `produces a compact JSON object` = {
      val output = new ByteArrayOutputStream
      provider.writeTo(coda, null, null, null, MediaType.APPLICATION_JSON_TYPE, null, output)

      output.toString.must(be(entity))
    }
  }
}
