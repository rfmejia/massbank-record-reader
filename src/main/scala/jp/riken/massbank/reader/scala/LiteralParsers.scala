package jp.riken.massbank.reader.scala

import java.time.LocalDate
import jp.riken.massbank.reader.scala.types._
import scala.util.parsing.combinator.JavaTokenParsers

/** Parsers for additional literals in MassBank */
trait LiteralParsers extends JavaTokenParsers {
  def anyString: Parser[String] = ".+".r ^^ (_.trim)

  def integer: Parser[Int] = wholeNumber ^^ (_.toInt)

  def double: Parser[Double] = floatingPointNumber ^^ (_.toDouble)

  def date: Parser[LocalDate] = "[\\d]{4,}".r ~ "." ~ "[\\d]{1,2}".r ~ "." ~ "[\\d]{1,2}".r ^^ {
    case year ~ "." ~ month ~ "." ~ day => LocalDate.of(year.toInt, month.toInt, day.toInt)
  }

  def subtag: Parser[(String, String)] = """[\w][\w\d_-]*""".r ~ anyString ^^ { case subtag ~ value => (subtag, value) }

  def peak(format: String): Parser[Peak] = format.split(" ").toList match {
    case "m/z" :: "int." :: "rel.int." :: Nil => double ~ double ~ double ^^ { case a ~ b ~ c => CompletePeakTriple(a, b, c) }
    case "m/z" :: "int." :: Nil               => double ~ double ^^ { case a ~ b => AbsolutePeakPair(a, b) }
    case "m/z" :: "rel.int." :: Nil           => double ~ double ^^ { case a ~ b => RelativePeakPair(a, b) }
    case _                                    => failure(s"Invalid peak data format '$format'")
  }
}

object LiteralParsers extends LiteralParsers
