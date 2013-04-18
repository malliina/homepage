import com.github.siasia.WebPlugin.webSettings
import com.github.siasia.PluginKeys._
import com.mle.util.Util
import sbt._
import cloudbees.Plugin.{CloudBees, cloudBeesSettings}
import HomeDependencies._
import sbt.Keys._
import scala.Some

/**
 * @author Mle
 */

object GitBuild extends Build {
  lazy val homePage = Project("homepage", file("."), settings = commonSettings)
    .settings(libraryDependencies ++= Seq(scalaTest, util, utilWeb, warDep))
    .settings(cloudBeesSettings: _*)
    .settings(
    CloudBees.useDeltaWar := false,
    CloudBees.applicationId := Some("home"),
    CloudBees.apiKey := beesConfig get "bees.api.key",
    CloudBees.apiSecret := beesConfig get "bees.api.secret",
    CloudBees.username := beesConfig get "bees.project.app.domain")
    .settings(myWebSettings:_*)

  val credentialPath = Path.userHome / ".sbt" / "credentials.txt"
  val credentialSettings =
    if (credentialPath.exists())
      Seq(credentials += Credentials(credentialPath))
    else Seq.empty
  val beesConfig = Util.optionally(
    Util.props((Path.userHome / ".bees" / "bees.config").toString)
  ).getOrElse(Map.empty)
  val myWebSettings = webSettings ++ Seq(
    webappResources in Compile <+= (sourceDirectory in Runtime)(sd => sd / "resources" / "publicweb")
  )
  val commonSettings = Defaults.defaultSettings ++ Seq(
    organization := "com.mle",
    version := "0.678-SNAPSHOT",
    scalaVersion := "2.10.0",
    retrieveManaged := false,
    resolvers += "Sonatype snaps" at "http://oss.sonatype.org/content/repositories/snapshots/",
    publishTo := Some(Resolver.url("my-sbt-releases", new URL("http://xxx/artifactory/my-sbt-releases/"))(Resolver.ivyStylePatterns)),
    publishMavenStyle := false,
    // system properties seem to have no effect in tests,
    // causing e.g. tests requiring javax.net.ssl.keyStore props to fail
    // ... unless fork is true
    sbt.Keys.fork in Test := true,
    exportJars := false
  ) ++ credentialSettings



}