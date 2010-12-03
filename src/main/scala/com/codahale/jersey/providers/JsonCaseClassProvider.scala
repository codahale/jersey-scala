package com.codahale.jersey.providers

import scala.reflect.Manifest
import java.io.{InputStream, OutputStream}
import java.lang.annotation.Annotation
import java.lang.reflect.Type
import javax.ws.rs.{WebApplicationException, Produces, Consumes}
import javax.ws.rs.core.Response.Status
import javax.ws.rs.core.{Response, MultivaluedMap, MediaType}
import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider
import javax.ws.rs.ext.Provider
import com.codahale.jerkson.{ParsingException, Json}

@Provider
@Produces(Array(MediaType.APPLICATION_JSON))
@Consumes(Array(MediaType.APPLICATION_JSON))
class JsonCaseClassProvider extends AbstractMessageReaderWriterProvider[Product] {
  def writeTo(json: Product, t: Class[_], genericType: Type, annotations: Array[Annotation],
              mediaType: MediaType, httpHeaders: MultivaluedMap[String, AnyRef],
              entityStream: OutputStream) {
    Json.generate(json, entityStream)
  }

  def isWriteable(t: Class[_], genericType: Type, annotations: Array[Annotation],
                  mediaType: MediaType) =
    mediaType == MediaType.APPLICATION_JSON_TYPE &&
            classOf[Product].isAssignableFrom(t)

  def readFrom(t: Class[Product], genericType: Type,
               annotations: Array[Annotation], mediaType: MediaType,
               httpHeaders: MultivaluedMap[String, String],
               entityStream: InputStream): Product = {
    try {
      Json.parse(entityStream)(Manifest.classType(t))
    } catch {
      case e: ParsingException => {
        throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
                                          .entity(e.getMessage)
                                          .build)
      }
    }
  }

  def isReadable(t: Class[_], genericType: Type, annotations: Array[Annotation],
                 mediaType: MediaType): Boolean =
    mediaType == MediaType.APPLICATION_JSON_TYPE &&
            classOf[Product].isAssignableFrom(t)
}
