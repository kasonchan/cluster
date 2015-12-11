val name = "cluster"

lazy val buildSettings = Seq(
  version := "0.1.0",
  scalaVersion := "2.11.7"
)

lazy val compilerOptions = Seq(
  "-encoding", "UTF-8",
  "-feature"
)

val testDependencies = Seq(
  "org.scalacheck" %% "scalacheck" % "1.12.5",
  "org.scalatest" %% "scalatest" % "2.2.5"
)

val akkaV = "2.4.1"
val akkaStreamV = "1.0"
val equationsV = "0.1.1"

val baseSettings = Seq(
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-cluster" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV,
    "com.typesafe.akka" %% "akka-stream-experimental" % akkaStreamV,
    "com.kasonchan" %% "equations" % equationsV
  ) ++ testDependencies.map(_ % "test"),
  scalacOptions in(Compile, console) := compilerOptions
)

lazy val allSettings = baseSettings ++ buildSettings

lazy val cluster = project.in(file("."))
  .settings(moduleName := name)
  .settings(allSettings: _*)
  .settings(
    libraryDependencies ++= testDependencies
  )
