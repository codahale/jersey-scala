package com.codahale.jersey.providers

import scala.reflect.Manifest
import java.io.{InputStreamReader, InputStream, OutputStreamWriter, OutputStream}
import java.lang.annotation.Annotation
import java.lang.reflect.Type
import javax.ws.rs.{WebApplicationException, Produces, Consumes}
import javax.ws.rs.core.Response.Status
import javax.ws.rs.core.{Response, MultivaluedMap, MediaType}
import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider
import javax.ws.rs.ext.Provider
import net.liftweb.json.JsonParser.{ParseException, parse}
import net.liftweb.json.{MappingException, NoTypeHints, Serialization}

@Provider
@Produces(Array(MediaType.APPLICATION_JSON))
@Consumes(Array(MediaType.APPLICATION_JSON))
class JsonCaseClassProvider extends AbstractMessageReaderWriterProvider[Product] {
  implicit val formats = Serialization.formats(NoTypeHints)

  def writeTo(json: Product, t: Class[_], genericType: Type, annotations: Array[Annotation],
              mediaType: MediaType, httpHeaders: MultivaluedMap[String, AnyRef],
              entityStream: OutputStream) {
    val writer = new OutputStreamWriter(entityStream)
    try {
      Serialization.write(json, writer)
    } finally {
      writer.close()
    }
  }

  def isWriteable(t: Class[_], genericType: Type, annotations: Array[Annotation],
                  mediaType: MediaType) =
    mediaType == MediaType.APPLICATION_JSON_TYPE &&
            classOf[Product].isAssignableFrom(t)

  def readFrom(t: Class[Product], genericType: Type,
               annotations: Array[Annotation], mediaType: MediaType,
               httpHeaders: MultivaluedMap[String, String],
               entityStream: InputStream): Product = {

    val reader = new InputStreamReader(entityStream)
    try {
      val json = parse(reader)
      json.extract(formats, Manifest.classType(t))
    } catch {
      case e @ (_: ParseException | _: MappingException) =>
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
                 mediaType: MediaType): Boolean =
    mediaType == MediaType.APPLICATION_JSON_TYPE &&
            classOf[Product].isAssignableFrom(t)
}
