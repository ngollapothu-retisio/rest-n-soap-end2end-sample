lazy val stolenLib = project
  .in(file("jar"))
  .settings(
    organization              := "com.example",
    name                      := "axis2ws-client-jar-sample",
    version                   := "0.1.0-SNAPSHOT",
    crossPaths                := false,  //don't add scala version to this artifacts in repo
    publishMavenStyle         := true,
    autoScalaLibrary          := false,  //don't attach scala libs as dependencies
    description               := "project for publishing dependency to maven repo, use 'sbt publishLocal' to install it",
    packageBin in Compile     := baseDirectory.value / s"${name.value}-${version.value}.jar"
  )