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

  def peakField(tag: String): Parser[PeakData] = tag ~> ":" ~> anyString ~ anyString.* ^^ {
    case format ~ lines =>
      val peakParser = peak(format)
      val peaks: List[Peak] = lines.map(l => parse(peakParser, l)).filter(_.successful).map(_.get)
      PeakData(peaks)
  }
}

object FieldParsers extends FieldParsers
