package com.codahale.jersey

import scala.collection.mutable.Builder
import javax.ws.rs.QueryParam
import javax.ws.rs.ext.Provider
import com.sun.jersey.api.model.Parameter
import com.sun.jersey.core.spi.component.{ComponentScope, ComponentContext}
import com.sun.jersey.spi.inject.{Injectable, InjectableProvider}

@Provider
class ScalaCollectionsQueryParamInjectableProvider extends InjectableProvider[QueryParam, Parameter] {
  def getScope = ComponentScope.PerRequest
  def getInjectable(ic: ComponentContext, a: QueryParam, c: Parameter): Injectable[_] = {
    val parameterName = c.getSourceName()
    if (parameterName != null && !parameterName.isEmpty) {
      val builder = build(c.getParameterClass)
      if (builder != null) {
        new ScalaCollectionQueryParamInjectable(
          new ScalaCollectionParameterExtractor(parameterName, c.getDefaultValue, builder),
          !c.isEncoded
        )
      } else null
    } else null
  }

  private def build(klass: Class[_]): Builder[String, _ <: Object] = {
         if (klass == classOf[Seq[String]])        Seq.newBuilder[String]
    else if (klass == classOf[List[String]])       List.newBuilder[String]
    else if (klass == classOf[Vector[String]])     Vector.newBuilder[String]
    else if (klass == classOf[IndexedSeq[String]]) IndexedSeq.newBuilder[String]
    else if (klass == classOf[Set[String]])        Set.newBuilder[String]
    else null
  }
}
