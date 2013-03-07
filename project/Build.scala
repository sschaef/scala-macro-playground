import sbt._
import Keys._

object BuildSettings {

  private lazy val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "org.scalamacros",
    version := "1.0.0",
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Xlint",
      "-Xlog-reflective-calls",
      "-Ywarn-adapted-args",
      "-encoding", "UTF-8",
      "-target:jvm-1.6"
      //"-Ymacro-debug-lite"
    ),
    scalaVersion := "2.11.0-SNAPSHOT",
    scalaOrganization := "org.scala-lang.macro-paradise",
    resolvers += Resolver.sonatypeRepo("snapshots")
  )

  private lazy val buildNormal = buildSettings ++ Seq(
    libraryDependencies <+= (scalaVersion)("org.scala-lang.macro-paradise" % "scala-reflect" % _)
    //libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _)
  )

  private lazy val buildIntroduceMember = {
    val path = file("/home/antoras/tmp/kepler-topic-introduce-member/pack")

    buildSettings ++ Seq(
      scalaHome := Some(path),
      unmanagedBase := path,
      unmanagedJars in Compile <<= baseDirectory map { base =>
        (path / "lib" ** "*.jar").classpath
      }
    )
  }

  lazy val build = buildIntroduceMember
}

object MyBuild extends Build {
  import BuildSettings._

  lazy val root: Project = Project(
    "root",
    file("core"),
    settings = build
  ) aggregate(macros, core)

  lazy val macros: Project = Project(
    "macros",
    file("macros"),
    settings = build
  )

  lazy val core: Project = Project(
    "core",
    file("core"),
    settings = build
  ) dependsOn(macros)
}