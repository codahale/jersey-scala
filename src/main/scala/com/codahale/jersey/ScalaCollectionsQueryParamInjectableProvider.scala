package com.codahale.jersey

import scala.collection.mutable.Builder
import javax.ws.rs.QueryParam
import javax.ws.rs.ext.Provider
import com.sun.jersey.api.model.Parameter
import com.sun.jersey.core.spi.component.{ComponentScope, ComponentContext}
import com.sun.jersey.spi.inject.{Injectable, InjectableProvider}

@Provider
class ScalaCollectionsQueryParamInjectableProvider extends InjectableProvider[QueryParam, Parameter] {
  private val builders = Map[Class[_], Builder[String, _ <: Object]](
    classOf[Seq[String]] -> Seq.newBuilder[String],
    classOf[List[String]] -> List.newBuilder[String],
    classOf[Vector[String]] -> Vector.newBuilder[String],
    classOf[IndexedSeq[String]] -> IndexedSeq.newBuilder[String],
    classOf[Set[String]] -> Set.newBuilder[String]
  )

  def getScope = ComponentScope.PerRequest
  def getInjectable(ic: ComponentContext, a: QueryParam, c: Parameter): Injectable[_] = {
    val parameterName = c.getSourceName()
    if (parameterName != null && !parameterName.isEmpty) {
      val builder = builders.get(c.getParameterClass)
      if (builder.isDefined) {
        new ScalaCollectionQueryParamInjectable(
          new ScalaCollectionParameterExtractor(parameterName, c.getDefaultValue, builder.get),
          !c.isEncoded
        )
      } else null
    } else null
  }
}
