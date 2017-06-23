name := """snakes-ladders"""
organization := "org.kastoestoramadus"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies += filters
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.1.0"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "org.kastoestoramadus.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "org.kastoestoramadus.binders._"
