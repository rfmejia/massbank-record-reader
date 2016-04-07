package jp.riken.massbank.reader.scala

import jp.riken.massbank.reader.scala.types._
import org.scalatest._
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class FieldParsersTest extends WordSpec with GeneratorDrivenPropertyChecks with Matchers with FieldParsers {
  "`peakField` field parser" should {
    "correctly parse multiple lines of peaks" in {
      val input = """PK$PEAK: m/z int. rel.int.
                    |  326.65 5.3 5
                    |  328.28 7.6 7""".stripMargin
      val result = parse(peakField("PK$PEAK"), input)
      result shouldBe a[Success[_]]
      result.get.peaks.size shouldBe 2
      result.get.peaks shouldBe Seq(CompletePeakTriple(326.65, 5.3, 5.0), CompletePeakTriple(328.28, 7.6, 7))
    }
  }

  "`numPeakField` parser" should {
    "accept integer inputs" in {
      forAll(Generators.validInt) { i: Int =>
        val result = parse(numPeakField("PK$NUM_PEAK"), "PK$NUM_PEAK: " + i.toString)
        result shouldBe a[Success[_]]
        result.get shouldBe Some(i)
      }
    }

    "reject non-integer inputs" in {
      forAll(Generators.validString) { s: String =>
        val result = parseAll(numPeakField("PK$NUM_PEAK"), "PK$NUM_PEAK: " + s)
        result shouldBe a[Success[_]]
        result.get shouldBe None
      }
    }
  }
}
