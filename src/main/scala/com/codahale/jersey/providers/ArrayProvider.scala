package com.codahale.jersey.providers

import javax.ws.rs.ext.Provider
import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider
import java.lang.reflect.Type
import java.lang.annotation.Annotation
import java.io.{IOException, InputStream, OutputStream}
import org.slf4j.LoggerFactory
import com.codahale.jerkson.{ParsingException, Json}
import javax.ws.rs.{WebApplicationException, Consumes, Produces}
import javax.ws.rs.core.{Response, MultivaluedMap, MediaType}
import javax.ws.rs.core.Response.Status
import scala.reflect.Manifest

@Provider
@Produces(Array(MediaType.APPLICATION_JSON))
@Consumes(Array(MediaType.APPLICATION_JSON))
class ArrayProvider[A] extends AbstractMessageReaderWriterProvider[Array[A]] {
  private val logger = LoggerFactory.getLogger(classOf[ArrayProvider[A]])

  def readFrom(klass: Class[Array[A]],
               genericType: Type,
               annotations: Array[Annotation],
               mediaType: MediaType,
               httpHeaders: MultivaluedMap[String, String],
               entityStream: InputStream) = {
    try {
      Json.parse(entityStream)(Manifest.classType(klass))
    } catch {
      case e: ParsingException => {
        throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
          .entity(e.getMessage)
          .build)
      }
    }
  }

  def isReadable(klass: Class[_],
                 genericType: Type,
                 annotations: Array[Annotation],
                 mediaType: MediaType) = klass.isArray

  def writeTo(t: Array[A],
              klass: Class[_],
              genericType: Type,
              annotations: Array[Annotation],
              mediaType: MediaType,
              httpHeaders: MultivaluedMap[String, AnyRef],
              entityStream: OutputStream) {
    try {
      Json.generate(t, entityStream)
    } catch {
      case e: IOException => logger.debug("Error writing to stream", e)
      case e => logger.error("Error encoding %s as JSON".format(t.mkString("Array(", ",", ")")), e)
    }
  }

  def isWriteable(klass: Class[_],
                  genericType: Type,
                  annotations: Array[Annotation],
                  mediaType: MediaType) = klass.isArray
}
