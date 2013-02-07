import sbt._

/**
 * @author Mle
 */

object HomeDependencies {
  val warDep = "org.mortbay.jetty" % "jetty" % "6.1.22" % "container"
  val scalaTest = "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  val util = "com.mle" %% "util" % "0.68-SNAPSHOT"
  val utilWeb = "com.mle" %% "util-web" % "0.66-SNAPSHOT"
}