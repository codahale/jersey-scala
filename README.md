Jersey-Scala
============

*Life's too short to use `java.util.Collection`*

Jersey-Scala is a set of classes which add Scala interoperation to Jersey.


Requirements
------------

* Scala 2.8.1 or 2.9.0-1 or 2.9.1
* Jerkson 0.4.2
* Jersey 1.9.1
* Slf4j API 1.6.2


How To Use
----------

**First**, specify Jersey-Scala as a dependency:

```xml
<dependency>
    <groupId>com.codahale</groupId>
    <artifactId>jersey-scala_${scala.version}</artifactId>
    <version>0.2.0</version>
</dependency>
```

**Second**, write your resource classes:

```scala
@Path("/things")
@Produces(Array("text/plain"))
class Things {
  @GET
  def getAThing(@QueryParam("name") names: Set[String]) = "I found: " + names.mkString(", ")
}
```
    


What All This Supports
----------------------

* `QueryParam`-annotated parameters of type `Seq[String]`, `List[String]`,
  `Vector[String]`, `IndexedSeq[String]`, `Set[String]`, and `Option[String]`.
* `AST.JValue` request and response entities.
* `JsonNode` request and response entities.
* Case class (i.e., `Product` instances) JSON request and response entities.
* `Array[A]` request and response entities. (Due to the JVM's type erasure and
  mismatches between Scala and Java type signatures, this is the only "generic"
  class supported since `Array` type parameters are reified.)


License
-------

Copyright (c) 2010-2011 Coda Hale

Published under The MIT License, see LICENSE
