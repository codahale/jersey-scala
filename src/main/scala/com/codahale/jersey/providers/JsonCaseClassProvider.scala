package com.codahale.jersey.providers

import scala.reflect.Manifest
import java.lang.annotation.Annotation
import java.lang.reflect.Type
import javax.ws.rs.{WebApplicationException, Produces, Consumes}
import javax.ws.rs.core.Response.Status
import javax.ws.rs.core.{Response, MultivaluedMap, MediaType}
import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider
import javax.ws.rs.ext.Provider
import com.codahale.jerkson.{ParsingException, Json}
import org.slf4j.LoggerFactory
import java.io.{IOException, InputStream, OutputStream}

@Provider
@Produces(Array(MediaType.APPLICATION_JSON))
@Consumes(Array(MediaType.APPLICATION_JSON))
class JsonCaseClassProvider extends AbstractMessageReaderWriterProvider[Product] {
  private val logger = LoggerFactory.getLogger(classOf[JsonCaseClassProvider])

  def writeTo(json: Product, t: Class[_], genericType: Type, annotations: Array[Annotation],
              mediaType: MediaType, httpHeaders: MultivaluedMap[String, AnyRef],
              entityStream: OutputStream) {
    try {
      Json.generate(json, entityStream)
    } catch {
      case e: IOException => logger.debug("Error writing to stream", e)
      case e => logger.error("Error encoding %s as JSON".format(json), e)
    }
  }

  def isWriteable(t: Class[_], genericType: Type, annotations: Array[Annotation],
                  mediaType: MediaType) =
    MediaType.APPLICATION_JSON_TYPE.isCompatible(mediaType) &&
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
      case e => {
        e match {
          case e: IOException => logger.debug("Error reading from stream", e)
          case _ => logger.error("Error decoding JSON request entity", e)
        }
        throw e
      }
    }
  }

  def isReadable(t: Class[_], genericType: Type, annotations: Array[Annotation],
                 mediaType: MediaType): Boolean =
    MediaType.APPLICATION_JSON_TYPE.isCompatible(mediaType) &&
            classOf[Product].isAssignableFrom(t)
}
