Jersey-Scala
============

*Life's too short to use `java.util.Collection`*

Jersey-Scala is a set of classes which add Scala interoperation to Jersey.


Requirements
------------

* Scala 2.8.1
* Jerkson 0.1.6
* Jersey 1.6
* Slf4j API 1.6.1


How To Use
----------

**First**, specify Jersey-Scala as a dependency:
    
    val codaRepo = "Coda Hale's Repository" at "http://repo.codahale.com/"
    val jerseyScala = "com.codahale" %% "jersey-scala" % "0.1.3" withSources()

**Second**, write your resource classes:
    
    @Path("/things")
    @Produces("text/plain")
    class Things {
      @GET
      def getAThing(@QueryParam("name") names: Set[String]) = "I found: " + names.mkString(", ")
    }
    


What All This Supports
----------------------

* `QueryParam`-annotated parameters of type `Seq[String]`, `List[String]`,
  `Vector[String]`, `IndexedSeq[String]`, `Set[String]`, and `Option[String]`.
* `AST.JValue` request and response entities.
* `JsonNode` request and response entities.
* Case class (i.e., `Product` instances) JSON request and response entities.


License
-------

Copyright (c) 2010-2011 Coda Hale

Published under The MIT License, see LICENSE
