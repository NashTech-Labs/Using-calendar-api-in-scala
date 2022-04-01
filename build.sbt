ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "calendar-api-scala",
    libraryDependencies ++= Seq(
      "com.google.api-client" % "google-api-client" % "1.33.2",
      "com.google.apis" % "google-api-services-calendar" % "v3-rev111-1.19.0",
      "com.google.oauth-client" % "google-oauth-client-jetty" % "1.33.1"
    )
  )

