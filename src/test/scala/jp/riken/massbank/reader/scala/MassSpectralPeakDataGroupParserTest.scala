package jp.riken.massbank.reader.scala

import jp.riken.massbank.reader.scala.groups._
import jp.riken.massbank.reader.scala.types.{ CompletePeakTriple, PeakData }
import org.scalatest._

class MassSpectralPeakDataGroupParserTest extends WordSpec with Matchers with MassSpectralPeakDataGroupParser {
  "An `MassSpectralPeakDataGroupParser`" should {
    "correctly parse complete base metadata" in {
      val input = """PK$SPLASH: splash10-00ke60z100-113be5f50f91fd032b18
                    |PK$ANNOTATION: 82.9455304521 118.065674262 119.0734992941 165.0664025435 170.0605888841 206.0372666262 229.0061193362 241.0061193362 257.9962829314 274.9990225856 286.9990225856 305.0095872719 323.0201519582
                    |PK$NUM_PEAK: 3
                    |PK$PEAK: m/z int. rel.int.
                    |  646.3223 64380108 999
                    |  647.3252 26819201 416
                    |  648.3309 7305831 113""".stripMargin

      val expected = MassSpectralPeakDataGroup(
        Some("splash10-00ke60z100-113be5f50f91fd032b18"),
        Some("82.9455304521 118.065674262 119.0734992941 165.0664025435 170.0605888841 206.0372666262 229.0061193362 241.0061193362 257.9962829314 274.9990225856 286.9990225856 305.0095872719 323.0201519582"),
        3,
        PeakData(List(
          CompletePeakTriple(646.3223, 64380108, 999),
          CompletePeakTriple(647.3252, 26819201, 416),
          CompletePeakTriple(648.3309, 7305831, 113)
        ))
      )

      val result = parse(massSpectralPeakDataGroup, input)
      result shouldBe a[Success[_]]
      result.get shouldBe expected
    }

    "accept empty peak values (e.g., when handling merged spectra, 'PK$NUM_PEAK' and 'PK$PEAK' are 'N/A')" in {
      val input = """PK$SPLASH: splash10-00ke60z100-113be5f50f91fd032b18
                    |PK$NUM_PEAK: N/A
                    |PK$PEAK: m/z int. rel.int.
                    |  N/A""".stripMargin
      val expected = MassSpectralPeakDataGroup(
        Some("splash10-00ke60z100-113be5f50f91fd032b18"),
        None,
        0,
        PeakData(List.empty)
      )

      val result = parse(massSpectralPeakDataGroup, input)
      result shouldBe a[Success[_]]
      result.get shouldBe expected
    }

    "accept special 'PK$ANNOTATION' field formats (single line, multiple line)" in {
      fail("TODO")
    }

    "reject peaks when `NUM_PEAK` is different from number of parsed peaks" in {
      val input = """PK$NUM_PEAK: 2
                    |PK$PEAK: m/z int. rel.int.
                    |  646.3223 64380108 999
                    |  647.3252 26819201 416
                    |  648.3309 7305831 113""".stripMargin

      parse(massSpectralPeakDataGroup, input) shouldBe a[NoSuccess]
    }
  }

}
