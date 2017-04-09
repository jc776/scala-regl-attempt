version in ThisBuild := "0.0-SNAPSHOT"
scalaVersion in ThisBuild := "2.12.1"

enablePlugins(WorkbenchPlugin)
refreshBrowsers <<= refreshBrowsers.triggeredBy(fastOptJS in Compile in motorcycle)

val motorcycle = project.in(file("Motorcycle"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "motorcycle",
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.1",
      "com.lihaoyi" %%% "scalatags" % "0.6.3"
    )
  )
