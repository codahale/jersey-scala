package com.codahale.jersey.inject

import com.sun.jersey.core.spi.component.ComponentScope
import com.sun.jersey.server.impl.model.parameter.multivalued.MultivaluedParameterExtractor

abstract class AbstractScalaCollectionsParamInjectableProvider {
  def getScope = ComponentScope.PerRequest
  protected def buildExtractor(name: String, default: String, klass: Class[_]): MultivaluedParameterExtractor = {
    if (klass == classOf[Seq[String]]) {
      new ScalaCollectionStringReaderExtractor[Seq](name, default, Seq)
    } else if (klass == classOf[List[String]]) {
      new ScalaCollectionStringReaderExtractor[List](name, default, List)
    } else if (klass == classOf[Vector[String]]) {
      new ScalaCollectionStringReaderExtractor[Vector](name, default, Vector)
    } else if (klass == classOf[IndexedSeq[String]]) {
      new ScalaCollectionStringReaderExtractor[IndexedSeq](name, default, IndexedSeq)
    } else if (klass == classOf[Set[String]]) {
      new ScalaCollectionStringReaderExtractor[Set](name, default, Set)
    } else if (klass == classOf[Option[String]]) {
      new ScalaOptionStringExtractor(name, default)
    } else null
  }
}



