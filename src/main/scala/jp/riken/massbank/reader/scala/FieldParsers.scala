package jp.riken.massbank.reader.scala

import java.time.LocalDate
import scala.util.Try

trait FieldParsers extends LiteralParsers {
  def field[T](parser: Parser[T])(name: String): Parser[T] = name ~> ":" ~> parser ^^ {
    case value => value.asInstanceOf[T]
  }

  def stringField: String => Parser[String] = field(anyString) _

  def doubleField: String => Parser[Double] = field(double) _

  def intField: String => Parser[Int] = field(integer) _

  def dateField: String => Parser[LocalDate] = field(date) _

  def subtagField: String => Parser[(String, String)] = field(subtag) _

  /** Special field for PK$NUM_PEAK to handle non-integral (e.g. N/A) values */
  def numPeakField(name: String): Parser[Option[Int]] = name ~> ":" ~> anyString ^^ {
    case value => Try(value.toInt).toOption
  }

  def fieldStartingWith(prefix: String): Parser[(String, String)] = tag ~ ":" ~ anyString ^? {
    case name ~ ":" ~ value if name.startsWith(prefix) => name -> value
  }

  /** Creates a map of values, where collisions are handled using separate chaining */
  def collapseToMap(l: List[(String, String)]): Map[String, List[String]] = l.groupBy(_._1).map { case (k, v) => k -> v.map(_._2) }

  def fieldsStartingWith(prefix: String) = fieldStartingWith(prefix).* ^^ collapseToMap

  /** Helper functions to quickly extract field values */
  implicit class FieldMapExtensions(fields: Map[String, List[String]]) extends FieldParsers {
    def getValue(key: String): Option[String] = fields.get(key).flatMap(_.headOption)

    def getIterative(key: String): List[String] = fields.getOrElse(key, List.empty)

    def getSubtags(key: String): Map[String, String] = {
      val subtags = fields.getOrElse(key, List.empty)
      val results = subtags.map(f => parse(subtag, f)).filter(_.successful).map(_.get)
      results.toMap
    }
  }
}

object FieldParsers extends FieldParsers
