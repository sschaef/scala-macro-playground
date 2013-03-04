import sbt._
import Keys._

object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq(
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
      "-target:jvm-1.6",
      "-Ymacro-debug-lite"
    ),
    scalaHome := Some(file("/home/antoras/dev/Scala/scala/build/pack")),
    unmanagedBase := file("/home/antoras/dev/Scala/scala/build/pack"),
    unmanagedJars in Compile <<= baseDirectory map { base =>
      (file("/home/antoras/dev/Scala/scala/build/pack") / "lib" ** "*.jar").classpath
    },
    scalaVersion := "2.11.0-SNAPSHOT",
    scalaOrganization := "org.scala-lang.macro-paradise",
    resolvers += Resolver.sonatypeRepo("snapshots")
  )
}

object MyBuild extends Build {
  import BuildSettings._

  lazy val root: Project = Project(
    "root",
    file("core"),
    settings = buildSettings
  ) aggregate(macros, core)

  lazy val macros: Project = Project(
    "macros",
    file("macros"),
    settings = buildSettings ++ inConfig(config("macro"))(Defaults.configSettings) ++ Seq(
      //libraryDependencies <+= (scalaVersion)("org.scala-lang.macro-paradise" % "scala-reflect" % _))
      //libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _)
    )
  )

  lazy val core: Project = Project(
    "core",
    file("core"),
    settings = buildSettings
  ) dependsOn(macros)
}