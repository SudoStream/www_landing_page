name := """www_landing_page"""
organization := "zone.timetoteach"

version := "0.0.6"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "com.typesafe.play" %% "play-mailer" % "6.0.1"
libraryDependencies += "com.typesafe.play" %% "play-mailer-guice" % "6.0.1"
libraryDependencies += "io.netty" % "netty-all" % "4.1.15.Final"
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.1.0"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "zone.timetoteach.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "zone.timetoteach.binders._"
