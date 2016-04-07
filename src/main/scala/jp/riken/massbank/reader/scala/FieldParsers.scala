package jp.riken.massbank.reader.scala

import java.time.LocalDate

import jp.riken.massbank.reader.scala.types._

import scala.util.Try

trait FieldParsers extends LiteralParsers {
  def field[T](parser: Parser[T])(tag: String): Parser[T] = tag ~> ":" ~> parser ^^ {
    case value => value.asInstanceOf[T]
  }

  def stringField: String => Parser[String] = field(anyString) _

  def doubleField: String => Parser[Double] = field(double) _

  def intField: String => Parser[Int] = field(integer) _

  def dateField: String => Parser[LocalDate] = field(date) _

  def subtagField: String => Parser[(String, String)] = field(subtag) _

  def peakField(tag: String): Parser[PeakData] = tag ~> ":" ~> anyString ~ anyString.* ^^ {
    case format ~ lines =>
      val peakParser = peak(format)
      val peaks: List[Peak] = lines.map(l => parse(peakParser, l)).filter(_.successful).map(_.get)
      PeakData(peaks)
  }

  /** Special field for PK$NUM_PEAK to handle non-integral (e.g. N/A) values */
  def numPeakField(tag: String): Parser[Option[Int]] = tag ~> ":" ~> anyString ^^ {
    case value => Try(value.toInt).toOption
  }
}

object FieldParsers extends FieldParsers
