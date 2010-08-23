package com.codahale.jersey.providers

import java.lang.annotation.Annotation
import java.lang.reflect.Type
import java.io.{InputStreamReader, OutputStreamWriter, InputStream, OutputStream}
import javax.ws.rs.{WebApplicationException, Consumes, Produces}
import javax.ws.rs.core.{Response, MultivaluedMap, MediaType}
import javax.ws.rs.core.Response.Status
import javax.ws.rs.ext.Provider
import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider
import net.liftweb.json.JsonAST.{JValue, render}
import net.liftweb.json.JsonParser.{ParseException, parse}
import net.liftweb.json.Printer.{pretty, compact}

/**
 * A MessageBodyReader/Writer which supports reading and writing JValue
 * instances. Supports writing "application/json+pretty" output, which
 * pretty-prints the JSON values.
 *
 * @author coda
 */
@Provider
@Produces(Array(MediaType.APPLICATION_JSON, "application/json+pretty"))
@Consumes(Array(MediaType.APPLICATION_JSON))
class JValueProvider extends AbstractMessageReaderWriterProvider[JValue] {
  // TODO: Aug 23, 2010 <coda> -- Figure out how to use application/json;pretty=true
  // application/json+pretty is its own MIME type, which means it doesn't play
  // nicely with others. It'd be nice if the request header was passed into
  // the MessageBodyWriter, but it doesn't seem to be. (1.4?)

  def writeTo(json: JValue, t: Class[_], genericType: Type, annotations: Array[Annotation],
              mediaType: MediaType, httpHeaders: MultivaluedMap[String, AnyRef],
              entityStream: OutputStream) {
    val writer = new OutputStreamWriter(entityStream)
    try {
      val doc = render(json)
      if (mediaType.getSubtype.endsWith("+pretty")) {
        pretty(doc, writer)
      } else {
        compact(doc, writer)
      }
    } finally {
      writer.close()
    }
  }

  def isWriteable(t: Class[_], genericType: Type, annotations: Array[Annotation],
                  mediaType: MediaType) = classOf[JValue].isAssignableFrom(t)

  def readFrom(t: Class[JValue], genericType: Type, annotations: Array[Annotation],
               mediaType: MediaType, httpHeaders: MultivaluedMap[String, String],
               entityStream: InputStream) = {
    val reader = new InputStreamReader(entityStream)
    try {
      parse(reader)
    } catch {
      case e: ParseException =>
        throw new WebApplicationException(
          Response.status(Status.BAD_REQUEST)
                  .entity(e.getMessage)
                  .build
        )
    } finally {
      reader.close()
    }
  }

  def isReadable(t: Class[_], genericType: Type, annotations: Array[Annotation],
                 mediaType: MediaType) = classOf[JValue].isAssignableFrom(t)
}
