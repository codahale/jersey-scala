package com.codahale.jersey.params.specs

import com.codahale.simplespec.Spec
import com.codahale.simplespec.annotation.test
import com.codahale.jersey.params.IntParam
import javax.ws.rs.WebApplicationException

class IntParamSpec extends Spec {
  class `A valid int parameter` {
    val param = IntParam("40")

    @test def `has an int value` = {
      param.value.must(be(40))
    }
  }

  class `An invalid int parameter` {
    @test def `throws a WebApplicationException with an error message` = {
      evaluating {
        IntParam("poop")
      }.must(throwAnExceptionLike {
        case e: WebApplicationException => {
          val response = e.getResponse
          response.getStatus.must(be(400))
          response.getEntity.must(be("Invalid parameter: poop (Must be an integer value.)"))
        }
      })
    }
  }
}
