val akkaVersion = "2.6.0"
val akkaHttpVersion = "10.1.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  //  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  //  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  //  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  //  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  //play json integrate
  "de.heikoseeberger" %% "akka-http-play-json" % "1.23.0",
  "com.typesafe.play" %% "play-json" % "2.6.10",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test
)

libraryDependencies ++= Seq(
  "org.scaldi" %% "scaldi-akka" % "0.5.8",
  "org.scaldi" %% "scaldi-jsr330" % "0.5.9"
)

libraryDependencies ++= List(
  "com.h2database" % "h2" % "1.4.197"
)

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.2.3",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.3"
)

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.27"
)

libraryDependencies ++= Seq(
  "com.pauldijou" %% "jwt-play-json" % "1.1.0"
)
libraryDependencies ++= Seq("com.softwaremill.macwire" %% "macros" % "2.3.1" % "provided")

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"

libraryDependencies += "org.flywaydb" % "flyway-core" % "5.2.4"

libraryDependencies +=  "org.mongodb.scala" %% "mongo-scala-driver" % "2.6.0"

lazy val run12 = taskKey[Unit]("")
run12 := {
  main.CodeGeneratorImp.main(Array("--config-path-prefix", "backend/src/main/resources"))
}