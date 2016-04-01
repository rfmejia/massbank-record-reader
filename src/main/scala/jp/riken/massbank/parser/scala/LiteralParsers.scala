package jp.riken.massbank.parser.scala

import java.time.LocalDate
import jp.riken.massbank.parser.scala.types._
import scala.util.parsing.combinator.JavaTokenParsers

/** Parsers for additional literals in MassBank */
trait LiteralParsers extends JavaTokenParsers {
  def anyString: Parser[String] = ".+".r.map(_.trim)

  def double: Parser[Double] = floatingPointNumber.map(_.toDouble)

  def int: Parser[Int] = wholeNumber.map(_.toInt)

  def date: Parser[LocalDate] = "[\\d]{4}".r ~ "." ~ "[\\d]{1,2}".r ~ "." ~ "[\\d]{1,2}".r ^^ {
    case year ~ "." ~ month ~ "." ~ day => LocalDate.of(year.toInt, month.toInt, day.toInt)
  }

  def link: Parser[DatabaseLink] = ident ~ " " ~ anyString ^^ {
    case db ~ " " ~ link => (db, link).asInstanceOf[DatabaseLink]
  }

  def field[T](parser: Parser[T])(tag: String): Parser[T] = tag ~> ":" ~> parser ^^ {
    case value => value.asInstanceOf[T]
  }

  def stringField: String => Parser[String] = field(anyString) _

  def doubleField: String => Parser[Double] = field(double) _

  def intField: String => Parser[Int] = field(int) _

  def dateField: String => Parser[LocalDate] = field(date) _

  def linkField: String => Parser[DatabaseLink] = field(link) _

  def peakField(tag: String): Parser[PeakData] = {
    val peakParser: Parser[Peak] = tag ~> ":" ~> anyString flatMap {
      case format =>
        format.split(" ").toList match {
          case "m/z" :: "int" :: "rel.int." :: Nil => int ~ double ~ double ^^ { case a ~ b ~ c => Complete(a, b, c) }
          case "m/z" :: "int" :: Nil               => int ~ double ^^ { case a ~ b => AbsoluteOnly(a, b) }
          case "m/z" :: "rel.int." :: Nil          => int ~ double ^^ { case a ~ b => RelativeOnly(a, b) }
          case _                                   => failure(s"Invalid peak data format '$format'")
        }
    }

    peakParser.* ^^ { case peaks => PeakData(peaks) }
  }
}

object LiteralParsers extends LiteralParsers
