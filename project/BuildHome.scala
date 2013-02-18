import com.github.siasia.WebPlugin.webSettings
import com.github.siasia.PluginKeys._
import com.mle.util.Util
import sbt.Keys._
import sbt._
import cloudbees.Plugin.{CloudBees, cloudBeesSettings}
import HomeDependencies._

/**
 * @author Mle
 */

object GitBuild extends Build {
  val credentialPath = Path.userHome / ".sbt" / "credentials.txt"
  val credentialSettings =
    if (credentialPath.exists())
      Seq(credentials += Credentials(credentialPath))
    else Seq.empty
  val beesConfig = Util.optionally(
    Util.props((Path.userHome / ".bees" / "bees.config").toString)
  ).getOrElse(Map.empty)

  val commonSettings = Defaults.defaultSettings ++ Seq(
    organization := "com.mle",
    version := "0.67-SNAPSHOT",
    scalaVersion := "2.10.0",
    retrieveManaged := false,
    publishTo := Some(Resolver.url("my-sbt-releases", new URL("http://xxx/artifactory/my-sbt-releases/"))(Resolver.ivyStylePatterns)),
    publishMavenStyle := false,
    // system properties seem to have no effect in tests,
    // causing e.g. tests requiring javax.net.ssl.keyStore props to fail
    // ... unless fork is true
    sbt.Keys.fork in Test := true,
    exportJars := false
  ) ++ credentialSettings

  lazy val homePage = Project("homepage", file("."), settings = commonSettings)
    .settings(
    webappResources in Compile <+= (sourceDirectory in Runtime)(sd => sd / "resources" / "publicweb")
  ).settings(libraryDependencies ++= Seq(scalaTest, util, utilWeb, warDep))
    .settings(webSettings: _*)
    .settings(cloudBeesSettings: _*)
    .settings(
    CloudBees.useDeltaWar := false,
    CloudBees.applicationId := Some("hometest"),
    CloudBees.apiKey := beesConfig get "bees.api.key",
    CloudBees.apiSecret := beesConfig get "bees.api.secret",
    CloudBees.username := beesConfig get "bees.project.app.domain")
}