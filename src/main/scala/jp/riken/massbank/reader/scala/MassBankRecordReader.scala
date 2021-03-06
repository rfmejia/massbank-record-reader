package jp.riken.massbank.reader.scala

import jp.riken.massbank.reader.scala.parsers.MassBankRecordParser
import jp.riken.massbank.reader.scala.types.MassBankRecord
import scala.io.Source
import scala.util.Try

class MassBankRecordParsingException(val message: String) extends IllegalArgumentException(message)

object MassBankRecordReader extends MassBankRecordParser {
  def read(input: String): Try[MassBankRecord] = parseAll(massBankRecord, input) match {
    case Success(record, _)      => util.Success(record)
    case NoSuccess(error, input) => util.Failure(new MassBankRecordParsingException(error + " " + input.toString))
  }

  def read(src: Source): Try[MassBankRecord] = {
    val lines = src.getLines.mkString("\n")
    read(lines)
  }
}
