Jersey-Scala
============

*Life's too short to use `java.util.Collection`*

Jersey-Scala is a set of class which add Scala interoperation to Jersey.


Requirements
------------

* Scala 2.8.0
* Jersey 1.3


How To Use
----------

**First**, specify Jersey-Scala as a dependency:
    
    val codaRepo = "Coda Hale's Repository" at "http://repo.codahale.com/"
    val jerseyScala = "com.codahale" %% "jersey-scala_1.3" % "0.1-SNAPSHOT" withSources()

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
  `Vector[String]`, `IndexedSeq[String]`, and `Set[String]`.
* `JsonAST.JValue` request and response entities.
* Case class (i.e., `Product` instances) request and response entities.


License
-------

Copyright (c) 2010 Coda Hale

Published under The MIT License, see LICENSE