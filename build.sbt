val commonSetting = Seq(
  version := "0.1",
  scalaVersion := "2.12.10",
  organization := "per.csw"
)

addCommandAlias("mg", "migrations/runMain main.Main")
addCommandAlias("codeGen", "migrations/runMain main.CodeGeneratorImp")

lazy val utils =
  project
    .in(file("utils"))
    .settings(commonSetting: _*)
    .settings(
      libraryDependencies ++= Seq(
        "com.typesafe" % "config" % "1.4.0"
      )
    )

lazy val backend =
  project
    .in(file("backend"))
    .dependsOn(utils)
    .settings(commonSetting: _*)
    .settings(
      libraryDependencies ++= Seq(
        "org.typelevel" %% "cats-effect" % "2.1.2"
      ),
      javaOptions := Seq("-XX:+PrintGCDetails"),
      fork := true
    )

lazy val migrations =
  project.in(file("migrations")).settings(commonSetting: _*).dependsOn(utils)

lazy val root =
  (project in file("."))
    .aggregate(backend, migrations)
    .settings(
      name := "scala-service-simple",
      sourceDirectories := Nil
    )

//scalacOptions += "-Ymacro-debug-lite"
cancelable in Global := true
