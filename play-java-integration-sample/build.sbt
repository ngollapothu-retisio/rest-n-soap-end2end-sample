ThisBuild / organization := "com.example"
ThisBuild / scalaVersion  := "2.13.11"
ThisBuild / name := "play-java-integration-sample"

ThisBuild / libraryDependencySchemes +=
  "org.scala-lang.modules" %% "scala-java8-compat" % VersionScheme.Always


lazy val `play-java-integration-sample` = (project in file("."))
  .enablePlugins(PlayMinimalJava, LauncherJarPlugin)
  .settings(common)
  .aggregate(`play-java-integration-sample-api`, `play-java-integration-sample-impl`)
  .dependsOn(`play-java-integration-sample-impl`)

lazy val `play-java-integration-sample-api` = (project in file("play-java-integration-sample-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lombok,
      guice,
      javaWs,
      soapStubExample,
      axis2Jaxws,
      axis2Http,
      axis2Local,
      axis2Kernel,
      axis2Adb
    )
  )

lazy val `play-java-integration-sample-impl` = (project in file("play-java-integration-sample-impl"))
  .enablePlugins(JavaAgent)
  .settings(common)
  .settings(
    libraryDependencies ++= Seq(
      lombok
    )
  )
  .dependsOn(`play-java-integration-sample-api`)

val lombok = "org.projectlombok" % "lombok" % "1.18.2"
val soapStubExample = "com.example" % "axis2ws-client-jar-sample" % "0.1.0-SNAPSHOT"
val axis2Kernel = "org.apache.axis2" % "axis2-kernel" % "1.8.2"
val axis2Adb = "org.apache.axis2" % "axis2-adb" % "1.8.2"
val axis2Http = "org.apache.axis2" % "axis2-transport-http" % "1.8.2"
val axis2Local = "org.apache.axis2" % "axis2-transport-local" % "1.8.2"
val axis2Jaxws = "org.apache.axis2" % "axis2-jaxws" % "1.8.2"

def common = Seq(
  dockerExposedPorts := Seq(9000, 8558, 9091, 10001),
  dockerBaseImage := "openjdk:11.0.11-jre-slim",
  Compile / doc / sources := Seq.empty,
  Compile / javacOptions := Seq("-g", "-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation", "-parameters"),
  libraryDependencies ++= Dependencies.dependencies
)
Global / excludeLintKeys += dockerBaseImage
Global / excludeLintKeys += dockerExposedPorts