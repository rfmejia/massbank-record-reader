package jp.riken.massbank.reader.scala

import java.time.LocalDate
import jp.riken.massbank.reader.scala.types._

trait FieldParsers extends LiteralParsers {
  def field[T](parser: Parser[T])(tag: String): Parser[T] = tag ~> ":" ~> parser ^^ {
    case value => value.asInstanceOf[T]
  }

  def stringField: String => Parser[String] = field(anyString) _

  def doubleField: String => Parser[Double] = field(double) _

  def intField: String => Parser[Int] = field(integer) _

  def dateField: String => Parser[LocalDate] = field(date) _

  def dbLinkField: String => Parser[DatabaseLink] = field(dbLink) _

  def peakField(tag: String): Parser[PeakData] = {
    val peakParser: Parser[Peak] = tag ~> ":" ~> anyString flatMap { case fmt => peak(fmt) }
    peakParser.* ^^ { case peaks => PeakData(peaks) }
  }
}

object FieldParsers extends FieldParsers
