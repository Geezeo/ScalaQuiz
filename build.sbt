javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

name := """geezeo-katas"""

version := "1.0"
scalaVersion := "2.11.12"

libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test
scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")
