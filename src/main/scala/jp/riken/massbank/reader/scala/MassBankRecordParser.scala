package jp.riken.massbank.reader.scala

import jp.riken.massbank.reader.scala.types._
import scala.util.parsing.combinator.JavaTokenParsers
import scala.util.{ Either, Left, Right }

/** Base ADT types and overall parsing structure for the MassBank record format. */
trait MassBankRecordParser extends JavaTokenParsers
    with RecordSpecificGroupParser
    with ChemicalGroupParser
    with SampleGroupParser
    with AnalyticalChemistryGroupParser
    with MassSpectralDataGroupParser
    with MassSpectralPeakDataGroupParser {

  /** The reference specification version. */
  val version = "2.10 (2016-03-01)"

  def massBankRecord: Parser[MassBankRecord] =
    recordSpecificGroup.? ~
      chemicalGroup.? ~
      sampleGroup.? ~
      analyticalChemistryGroup.? ~
      massSpectralDataGroup.? ~
      massSpectralPeakDataGroup.? <~
      "//" ^^ {
        case base ~ ch ~ sp ~ ac ~ ms ~ pk => MassBankRecord(base, ch, sp, ac, ms, pk)
      }
}

object MassBankRecordParser extends MassBankRecordParser {
  def read(input: String): Either[String, MassBankRecord] = parseAll(massBankRecord, input) match {
    case Success(record, _)      => Right(record)
    case NoSuccess(error, input) => Left(error + " " + input.toString)
  }
}
