lazy val akkaHttpVersion      = "10.0.10"
lazy val akkaVersion          = "2.5.4"
lazy val circeVersion         = "0.8.0"
lazy val catsVersion          = "0.9.0"
lazy val akkaHttpCirceVersion = "1.18.0"
lazy val kindProjectorVersion = "0.9.4"
lazy val pureconfigVersion    = "0.8.0"
lazy val scalatestVersion     = "3.0.1"

lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "com.spotimap",
      scalaVersion := "2.12.4",
      version := "0.1.0"
    )),
  name := "spotimap",
  libraryDependencies ++= Seq(
    "com.typesafe.akka"     %% "akka-http"            % akkaHttpVersion,
    "de.heikoseeberger"     %% "akka-http-circe"      % akkaHttpCirceVersion,
    "com.typesafe.akka"     %% "akka-http-xml"        % akkaHttpVersion,
    "com.typesafe.akka"     %% "akka-stream"          % akkaVersion,
    "io.circe"              %% "circe-core"           % circeVersion,
    "io.circe"              %% "circe-generic"        % circeVersion,
    "io.circe"              %% "circe-generic-extras" % circeVersion,
    "io.circe"              %% "circe-parser"         % circeVersion,
    "org.spire-math"        %% "kind-projector"       % kindProjectorVersion,
    "org.typelevel"         %% "cats-core"            % catsVersion,
    "org.typelevel"         %% "cats-free"            % catsVersion,
    "com.github.pureconfig" %% "pureconfig"           % pureconfigVersion,
    "com.typesafe.akka"     %% "akka-http-testkit"    % akkaHttpVersion % Test,
    "org.scalatest"         %% "scalatest"            % scalatestVersion % Test
  )
)

addCompilerPlugin(("org.scalamacros" % "paradise" % "2.1.0").cross(CrossVersion.full))
