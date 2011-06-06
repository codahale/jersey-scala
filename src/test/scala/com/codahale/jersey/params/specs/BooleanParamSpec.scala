package com.codahale.jersey.params.specs

import com.codahale.simplespec.Spec
import com.codahale.jersey.params.BooleanParam
import javax.ws.rs.WebApplicationException

class BooleanParamSpec extends Spec {
  class `A valid boolean parameter` {
    private val param = BooleanParam("true")

    def `should have a boolean value` = {
      param.value must beTrue
    }
  }

  class `An invalid boolean parameter` {
    def `should throw a WebApplicationException with an error message` = {
      BooleanParam("poop") must throwA[WebApplicationException].like {
         case e: WebApplicationException =>
          val response = e.getResponse
          response.getStatus must beEqualTo(400)
          response.getEntity must beEqualTo("Invalid parameter: poop (Must be \"true\" or \"false\".)")
      }
    }
  }
}
