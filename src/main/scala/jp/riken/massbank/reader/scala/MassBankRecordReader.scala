package jp.riken.massbank.reader.scala

import jp.riken.massbank.reader.scala.parsers.MassBankRecordParser
import jp.riken.massbank.reader.scala.types.MassBankRecord

object MassBankRecordReader extends MassBankRecordParser {
  def read(input: String): Either[String, MassBankRecord] = parseAll(massBankRecord, input) match {
    case Success(record, _)      => Right(record)
    case NoSuccess(error, input) => Left(error + " " + input.toString)
  }
}
