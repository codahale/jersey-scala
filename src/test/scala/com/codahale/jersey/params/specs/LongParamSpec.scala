package com.codahale.jersey.params.specs

import com.codahale.simplespec.Spec
import com.codahale.simplespec.annotation.test
import com.codahale.jersey.params.LongParam
import javax.ws.rs.WebApplicationException

class LongParamSpec extends Spec {
  class `A valid long parameter` {
    private val param = LongParam("40")

    @test def `has an int value` = {
      param.value.must(be(40L))
    }
  }

  class `An invalid long parameter` {
    @test def `throws a WebApplicationException with an error message` = {
      evaluating {
        LongParam("poop")
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
