lazy val akkaHttpVersion = "10.0.9"
lazy val akkaVersion = "2.5.3"
lazy val circeVersion = "0.8.0"


lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.spotimap",
      scalaVersion := "2.12.3"
    )),
    name := "spotimap",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "de.heikoseeberger" %% "akka-http-circe" % "1.18.0",
      "com.typesafe.akka" %% "akka-http-xml" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,

      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "org.scalatest" %% "scalatest" % "3.0.1" % Test
    )
  )
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
