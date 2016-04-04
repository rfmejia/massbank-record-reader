import sbt._
import sbt.Keys._

object ProjectBuild extends Build {

  override def settings = super.settings ++ Seq(
    name := "massbank-record-reader",
    organization := "jp.riken",
    version := "2.10-1-SNAPSHOT",
    scalaVersion in ThisBuild := "2.11.8",
    scalacOptions ++= Seq(
      "-Xlint",
      "-deprecation",
      "-Xfatal-warnings",
      "-feature",
      "-unchecked",
      "-encoding", "utf8")
  )

  import MainDependencies._
  import TestDependencies._
  import ScalariformSettings._

  lazy val root = Project(id = "massbank-record-reader", base = file("."))
    .settings(
      customScalariformSettings,
      libraryDependencies ++= Seq(
        parserCombinators,
        scalatest,
        scalacheck
      )
    )
}

