name := "sutra-transformer"

version := "1.0"

scalaVersion := "2.11.7"

lazy val hello = taskKey[Unit]("An cbeta xml sutra transformer")

lazy val deps = Seq(
  "org.scala-lang" % "scala-xml" % "2.11.0-M4",
  "com.alibaba" % "fastjson" % "1.2.7"
)

lazy val root = (project in file(".")).enablePlugins(SbtNativePackager).settings(
  hello := {
    println("Hello!")
  },
  libraryDependencies ++= deps
)

