package com.codahale.jersey.providers

import java.lang.annotation.Annotation
import java.lang.reflect.Type
import java.io.{InputStream, OutputStream}
import javax.ws.rs.{WebApplicationException, Consumes}
import javax.ws.rs.core.{Response, MultivaluedMap, MediaType}
import javax.ws.rs.core.Response.Status
import javax.ws.rs.ext.Provider
import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider
import com.codahale.jerkson.AST.JValue
import com.codahale.jerkson.{Json, ParsingException}
import org.slf4j.LoggerFactory

/**
 * A MessageBodyReader/Writer which supports reading and writing JValue
 * instances. Supports writing "application/json+pretty" output, which
 * pretty-prints the JSON values.
 *
 * @author coda
 */
@Provider
@Consumes(Array(MediaType.APPLICATION_JSON))
class JValueProvider extends AbstractMessageReaderWriterProvider[JValue] {
  private val logger = LoggerFactory.getLogger(classOf[JValueProvider])

  def writeTo(json: JValue, t: Class[_], genericType: Type, annotations: Array[Annotation],
              mediaType: MediaType, httpHeaders: MultivaluedMap[String, AnyRef],
              entityStream: OutputStream) {
    try {
      Json.generate(json, entityStream)
    } catch {
      case e => logger.error("Error encoding %s as JSON".format(json), e)
    }
  }

  def isWriteable(t: Class[_], genericType: Type, annotations: Array[Annotation],
                  mediaType: MediaType) = classOf[JValue].isAssignableFrom(t)

  def readFrom(t: Class[JValue], genericType: Type, annotations: Array[Annotation],
               mediaType: MediaType, httpHeaders: MultivaluedMap[String, String],
               entityStream: InputStream) = {
    try {
      Json.parse[JValue](entityStream)
    } catch {
      case e: ParsingException => {
        throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
                                          .entity(e.getMessage)
                                          .build)
      }
      case e => {
        logger.error("Error decoding JSON request entity", e)
        throw e
      }
    }
  }

  def isReadable(t: Class[_], genericType: Type, annotations: Array[Annotation],
                 mediaType: MediaType) = classOf[JValue].isAssignableFrom(t)
}
