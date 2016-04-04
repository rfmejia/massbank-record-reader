package jp.riken.massbank.reader.scala

import java.time.LocalDate
import jp.riken.massbank.reader.scala.types._
import scala.util.parsing.combinator.JavaTokenParsers

/** Parsers for additional literals in MassBank */
trait LiteralParsers extends JavaTokenParsers {
  def anyString: Parser[String] = ".+".r ^^ (_.trim)

  def integer: Parser[Int] = wholeNumber ^^ (_.toInt)

  def double: Parser[Double] = floatingPointNumber ^^ (_.toDouble)

  def date: Parser[LocalDate] = "[\\d]{4}".r ~ "." ~ "[\\d]{1,2}".r ~ "." ~ "[\\d]{1,2}".r ^^ {
    case year ~ "." ~ month ~ "." ~ day => LocalDate.of(year.toInt, month.toInt, day.toInt)
  }

  def dbLink: Parser[DatabaseLink] = """[\w][\w\d_-]*""".r ~ anyString ^^ {
    case db ~ link => (db, link).asInstanceOf[DatabaseLink]
  }

  def field[T](parser: Parser[T])(tag: String): Parser[T] = tag ~> ":" ~> parser ^^ {
    case value => value.asInstanceOf[T]
  }

  def stringField: String => Parser[String] = field(anyString) _

  def doubleField: String => Parser[Double] = field(double) _

  def intField: String => Parser[Int] = field(integer) _

  def dateField: String => Parser[LocalDate] = field(date) _

  def dbLinkField: String => Parser[DatabaseLink] = field(dbLink) _

  def peakField(tag: String): Parser[PeakData] = {
    val peakParser: Parser[Peak] = tag ~> ":" ~> anyString flatMap {
      case format =>
        format.split(" ").toList match {
          case "m/z" :: "int" :: "rel.int." :: Nil => integer ~ double ~ double ^^ { case a ~ b ~ c => Complete(a, b, c) }
          case "m/z" :: "int" :: Nil               => integer ~ double ^^ { case a ~ b => AbsoluteOnly(a, b) }
          case "m/z" :: "rel.int." :: Nil          => integer ~ double ^^ { case a ~ b => RelativeOnly(a, b) }
          case _                                   => failure(s"Invalid peak data format '$format'")
        }
    }

    peakParser.* ^^ { case peaks => PeakData(peaks) }
  }
}

object LiteralParsers extends LiteralParsers
