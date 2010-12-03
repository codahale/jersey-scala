import sbt._

class JerseyScala(info: ProjectInfo) extends DefaultProject(info) with posterous.Publish with IdeaProject {
  /**
   * Publish the source as well as the class files.
   */
  override def packageSrcJar = defaultJarPath("-sources.jar")
  val sourceArtifact = Artifact.sources(artifactID)
  override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageSrc)

  /**
   * Publish via Ivy.
   */
  lazy val publishTo = Resolver.sftp("Personal Repo",
                                     "codahale.com",
                                     "/home/codahale/repo.codahale.com/") as ("codahale")
  override def managedStyle = ManagedStyle.Maven

  /**
   * Dependencies
   */
  val sunRepo = "Sun Repo" at "http://download.java.net/maven/2/"

  val jerkson = "com.codahale" %% "jerkson" % "0.1.0" withSources () intransitive ()
  val jacksonVersion = "1.6.2"
  val jacksonCore = "org.codehaus.jackson" % "jackson-core-asl" % jacksonVersion withSources () intransitive ()
  val jacksonMapper = "org.codehaus.jackson" % "jackson-mapper-asl" % jacksonVersion withSources () intransitive ()
  val paranamer = "com.thoughtworks.paranamer" % "paranamer" % "2.3" withSources () intransitive ()

  val jerseyCore = "com.sun.jersey" % "jersey-core" % "1.5-ea06" withSources() intransitive()
  val jerseyServer = "com.sun.jersey" % "jersey-server" % "1.5-ea06" withSources() intransitive()

  val codasRepo = "Coda's Repo" at "http://repo.codahale.com"
  val specs = "org.scala-tools.testing" %% "specs" % "1.6.6" % "test" withSources()
  val simplespec = "com.codahale" %% "simplespec" % "0.2.0" % "test" withSources()
  val mockito = "org.mockito" % "mockito-all" % "1.8.4" % "test" withSources()
}
