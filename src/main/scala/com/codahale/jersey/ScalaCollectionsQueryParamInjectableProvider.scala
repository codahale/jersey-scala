package com.codahale.jersey

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

  private def build(klass: Class[_]): CollectionBuilder[_ <: Object] = {
         if (klass == classOf[Seq[String]])        SeqCollectionBuilder
    else if (klass == classOf[List[String]])       ListCollectionBuilder
    else if (klass == classOf[Vector[String]])     VectorCollectionBuilder
    else if (klass == classOf[IndexedSeq[String]]) IndexedSeqCollectionBuilder
    else if (klass == classOf[Set[String]])        SetCollectionBuilder
    else null
  }
}
