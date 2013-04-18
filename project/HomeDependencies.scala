import sbt._

/**
 * @author Mle
 */

object HomeDependencies {
  val warDep = "org.mortbay.jetty" % "jetty" % "6.1.22" % "container"
  val scalaTest = "org.scalatest" %% "scalatest" % "1.9.1" % "test"
  val utilGroup = "com.github.malliina"
  val util = utilGroup %% "util" % "0.7.1"
  val utilWeb = utilGroup %% "util-web" % "0.69-SNAPSHOT"
}