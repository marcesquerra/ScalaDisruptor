name := "ScalaDisruptor"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % "2.10.3",
    "com.lmax" % "disruptor" % "3.2.1",
    "org.specs2" %% "specs2" % "2.3.12" % "test"
)

scalacOptions += "-language:experimental.macros"

