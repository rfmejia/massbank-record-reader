package jp.riken.massbank.reader.scala

import jp.riken.massbank.reader.scala.types._
import org.scalatest._

class FieldParsersTest extends WordSpec with Matchers with FieldParsers {
  "`peakField` field parser" should {
    "correctly parse multiple lines of peaks" in {
      val input = """PK$PEAK: m/z int. rel.int.
                    |  326.65 5.3 5
                    |  328.28 7.6 7""".stripMargin
      val result = parse(peakField("PK$PEAK"), input)
      println(input)
      println(result)
      result shouldBe a[Success[_]]
      result.get.peaks.size shouldBe 2
      result.get.peaks shouldBe Seq(CompletePeakTriple(326.65, 5.3, 5.0), CompletePeakTriple(328.28, 7.6, 7))
    }
  }
}
