import sbt.addCompilerPlugin

lazy val akkaHttpVersion      = "10.0.11"
lazy val circeVersion         = "0.8.0"
lazy val catsVersion          = "0.9.0"
lazy val akkaHttpCirceVersion = "1.18.1"
lazy val kindProjectorVersion = "0.9.5"
lazy val pureconfigVersion    = "0.8.0"
lazy val scalatestVersion     = "3.0.4"
lazy val macroParadiseVersion = "2.1.1"

lazy val commonDeps = Seq(
  "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
  "de.heikoseeberger" %% "akka-http-circe"      % akkaHttpCirceVersion,
  "io.circe"          %% "circe-core"           % circeVersion,
  "io.circe"          %% "circe-generic"        % circeVersion,
  "io.circe"          %% "circe-generic-extras" % circeVersion,
  "org.typelevel"     %% "cats-core"            % catsVersion,
  "org.typelevel"     %% "cats-free"            % catsVersion
)

lazy val clientDeps = commonDeps

lazy val appDeps = commonDeps ++ Seq(
  "com.github.pureconfig" %% "pureconfig"        % pureconfigVersion,
  "com.typesafe.akka"     %% "akka-http-testkit" % akkaHttpVersion % Test,
  "org.scalatest"         %% "scalatest"         % scalatestVersion % Test
)

lazy val macroParadise = addCompilerPlugin(("org.scalamacros" % "paradise" % macroParadiseVersion).cross(CrossVersion.full))
lazy val kindProjector = addCompilerPlugin("org.spire-math" %% "kind-projector" % kindProjectorVersion)

lazy val client = project.settings(
  organization := "com.kubukoz",
  scalaVersion := "2.12.4",
  version := "0.1.0",
  name := "spotify-client",
  libraryDependencies ++= clientDeps,
  macroParadise
)

lazy val app = project
  .settings(
    organization := "com.spotimap",
    scalaVersion := "2.12.4",
    version := "0.1.0",
    name := "spotimap-app",
    libraryDependencies ++= appDeps,
    kindProjector
  )
  .dependsOn(client)

lazy val spotimap = (project in file(".")).aggregate(client, app)
