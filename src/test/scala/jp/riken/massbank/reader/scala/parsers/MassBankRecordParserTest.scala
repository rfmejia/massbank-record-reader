package jp.riken.massbank.reader.scala.parsers

import jp.riken.massbank.reader.scala.{ MassBankRecordParsingException, MassBankRecordReader }
import jp.riken.massbank.reader.scala.types.MassBankRecord
import org.scalatest._

import scala.io.Source
import scala.util.Try

class MassBankRecordParserTest extends WordSpec with Matchers with MassBankRecordParser with Inside {
  "A `MassBankRecordParser`" should {

    "correctly parse sample data" in {
      val sources = Source.fromURL(getClass.getResource("/testdata"))
      sources should not be null

      def parseFiles(filename: String): Try[MassBankRecord] = {
        val source = Source.fromURL(getClass.getResource("/testdata/" + filename))
        source should not be null
        MassBankRecordReader.read(source)
      }

      val filenames = sources.getLines
      filenames.map(parseFiles).foreach(_ shouldBe a[util.Success[_]])
    }

    // TODO Must write more tests to simulate failure cases, this test is not enough for coverage
    "reject empty input" in {
      val result = MassBankRecordReader.read("")
      result shouldBe a[util.Failure[_]]
      intercept[MassBankRecordParsingException] { result.get }
    }

  }
}
