package com.codahale.jersey.params.specs

import com.codahale.simplespec.Spec
import javax.ws.rs.WebApplicationException
import com.codahale.jersey.params.LongParam

object LongParamSpec extends Spec {
  class `A valid long parameter` {
    private val param = LongParam("40")

    def `should have an int value` = {
      param.value must beEqualTo(40L)
    }
  }

  class `An invalid long parameter` {
    def `should throw a WebApplicationException with an error message` = {
      LongParam("poop") must throwA[WebApplicationException].like {
         case e: WebApplicationException =>
          val response = e.getResponse
          response.getStatus must beEqualTo(400)
          response.getEntity must beEqualTo("Invalid parameter: poop (Must be an integer value.)")
      }
    }
  }
}
