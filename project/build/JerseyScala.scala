class JerseyScala(info: sbt.ProjectInfo) extends sbt.DefaultProject(info) with posterous.Publish with rsync.RsyncPublishing with IdeaProject {
  /**
   * Publish the source as well as the class files.
   */
  override def packageSrcJar= defaultJarPath("-sources.jar")
  val sourceArtifact = sbt.Artifact(artifactID, "src", "jar", Some("sources"), Nil, None)
  override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageSrc)

  /**
   * Publish via rsync.
   */
  def rsyncRepo = "codahale.com:/home/codahale/repo.codahale.com"

  /**
   * Dependencies
   */
  val sunRepo = "Sun Repo" at "http://download.java.net/maven/2/"
  
  val liftJson = "net.liftweb" % "lift-json_2.8.0" % "2.2-M1" withSources() intransitive()
  val paranamer = "com.thoughtworks.paranamer" % "paranamer" % "2.2.1" withSources() intransitive()

  val jaxrs = "javax.ws.rs" % "jsr311-api" % "1.1.1" withSources() intransitive()
  val jerseyCore = "com.sun.jersey" % "jersey-core" % "1.3" withSources() intransitive()
  val jerseyServer = "com.sun.jersey" % "jersey-server" % "1.3" withSources() intransitive()

  val codasRepo = "Coda's Repo" at "http://repo.codahale.com"
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.6" % "test" withSources()
  val simplespec = "com.codahale" %% "simplespec" % "0.2.0" % "test" withSources()
  val mockito = "org.mockito" % "mockito-all" % "1.8.4" % "test" withSources()
}
