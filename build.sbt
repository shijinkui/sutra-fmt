name := "sutra-transformer"

version := "1.0"

scalaVersion := "2.11.7"

lazy val hello = taskKey[Unit]("An cbeta xml sutra transformer")

lazy val deps = Seq(
  "org.scala-lang" % "scala-xml" % "2.11.0-M4",
  "com.alibaba" % "fastjson" % "1.2.7",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.3",
  "com.google.guava" % "guava" % "18.0",
  "org.slf4j" % "slf4j-api" % "1.7.7",
  "ch.qos.logback" % "logback-core" % "1.1.3",
  "ch.qos.logback" % "logback-classic" % "1.1.3"
)

lazy val root = (project in file(".")).enablePlugins(SbtNativePackager).settings(
  hello := {
    println("Hello!")
  },
  libraryDependencies ++= deps
)
