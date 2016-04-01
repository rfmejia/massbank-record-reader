import sbt._

object MainDependencies {
  val parserCombinators = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
}

object TestDependencies {
  val scalatest  = "org.scalatest"  %% "scalatest"  % "2.2.6"  % "test"
}

