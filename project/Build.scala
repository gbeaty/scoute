import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

import sbt._
import sbt.Keys._

object Scoute extends Build {
  lazy val scoute = crossProject.in(file("scoute")).
    settings(
      name := "scoute",
      version := "0.0.1",
      scalaVersion := "2.11.8",
      licenses += ("Unlicense", url("http://unlicense.org/UNLICENSE")),
      libraryDependencies ++= Seq(

      ),
      resolvers ++= Seq(
      )
    ).jvmSettings(
      libraryDependencies ++= Seq(
        "com.lihaoyi" %% "utest" % "0.3.1" % "test"
      )
    ).jsSettings(
      testFrameworks += new TestFramework("utest.runner.Framework"),
      libraryDependencies ++= Seq(
        "com.lihaoyi" %%% "utest" % "0.3.1" % "test"
      )
    )
}