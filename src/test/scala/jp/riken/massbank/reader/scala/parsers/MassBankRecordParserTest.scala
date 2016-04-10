package jp.riken.massbank.reader.scala.parsers

import jp.riken.massbank.reader.scala.MassBankRecordReader
import jp.riken.massbank.reader.scala.types.MassBankRecord
import org.scalatest._

import scala.io.Source

class MassBankRecordParserTest extends WordSpec with Matchers with MassBankRecordParser {
  "An `MassBankRecordGroupParser`" should {

    "correctly parse sample data" in {
      val sources = Source.fromURL(getClass.getResource("/testdata"))
      sources should not be null

      def parseFiles(filename: String): Either[String, MassBankRecord] = {
        val source = Source.fromURL(getClass.getResource("/testdata/" + filename))
        source should not be null
        MassBankRecordReader.read(source)
      }

      val filenames = sources.getLines
      filenames.map(parseFiles).foreach(_ shouldBe a[Right[_, _]])
    }

  }
}
