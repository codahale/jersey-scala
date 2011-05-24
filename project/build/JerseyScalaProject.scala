import sbt._

class JerseyScalaProject(info: ProjectInfo) extends DefaultProject(info)
                                                    with IdeaProject
                                                    with maven.MavenDependencies {
  /**
   * Publish the source as well as the class files.
   */
  override def packageSrcJar = defaultJarPath("-sources.jar")
  val sourceArtifact = Artifact.sources(artifactID)
  override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageSrc)

  lazy val publishTo = Resolver.sftp("Personal Repo",
                                     "codahale.com",
                                     "/home/codahale/repo.codahale.com/")

  /**
   * Always compile with deprecation alerts, full type explanations for errors,
   * full unchecked errors, and optimizations.
   */
  override def compileOptions = super.compileOptions ++
    Seq(Deprecation, ExplainTypes, Unchecked, Optimise)

  /**
   * Repositories
   */
  val sunRepo = "Sun Repo" at "http://download.java.net/maven/2/"
  val codasRepo = "Coda's Repo" at "http://repo.codahale.com"

  /**
   * Dependencies
   */
  val jerkson = "com.codahale" %% "jerkson" % "0.2.2"
  val jerseyServer = "com.sun.jersey" % "jersey-server" % "1.7"
  val slf4j = "org.slf4j" % "slf4j-api" % "1.6.1"

  /**
   * Test Dependencies
   */
  val simplespec = "com.codahale" %% "simplespec" % "0.3.3" % "test"
  def specs2Framework = new TestFramework("org.specs2.runner.SpecsFramework")
  override def testFrameworks = super.testFrameworks ++ Seq(specs2Framework)
}
