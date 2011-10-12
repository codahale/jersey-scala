package com.codahale.jersey.inject

import com.sun.jersey.api.ParamException
import com.sun.jersey.api.core.HttpContext
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable
import com.sun.jersey.server.impl.model.parameter.multivalued.{MultivaluedParameterExtractor, ExtractorContainerException}

class ScalaCollectionFormParamInjectable(extractor: MultivaluedParameterExtractor)
        extends AbstractHttpContextInjectable[Object] {

  def getValue(c: HttpContext) = try {
    extractor.extract(c.getRequest.getFormParameters)
  } catch {
    case e: ExtractorContainerException =>
      throw new ParamException.FormParamException(e.getCause,
                                                  extractor.getName,
                                                  extractor.getDefaultStringValue)
  }
}
