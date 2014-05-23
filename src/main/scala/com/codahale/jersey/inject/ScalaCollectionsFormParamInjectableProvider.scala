package com.codahale.jersey.inject

import javax.ws.rs.ext.Provider
import com.sun.jersey.api.model.Parameter
import com.sun.jersey.core.spi.component.{ComponentScope, ComponentContext}
import com.sun.jersey.spi.inject.{Injectable, InjectableProvider}
import com.sun.jersey.server.impl.model.parameter.multivalued.MultivaluedParameterExtractor
import javax.ws.rs.{FormParam, QueryParam}

@Provider
class ScalaCollectionsFormParamInjectableProvider
  extends AbstractScalaCollectionsParamInjectableProvider
  with InjectableProvider[FormParam, Parameter]
{
  def getInjectable(ic: ComponentContext, a: FormParam, c: Parameter): Injectable[_] = {
    val parameterName = c.getSourceName()
    if (parameterName != null && !parameterName.isEmpty) {
      buildInjectable(parameterName, c.getDefaultValue, c.getParameterClass)
    } else null
  }

  private def buildInjectable(name: String, default: String, klass: Class[_]): Injectable[_ <: Object] = {
    val extractor = buildExtractor(name, default, klass)
    if (extractor != null) {
      new ScalaCollectionFormParamInjectable(extractor)
    } else null
  }
}



