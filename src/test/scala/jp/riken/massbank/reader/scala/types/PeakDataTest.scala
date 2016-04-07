package jp.riken.massbank.reader.scala.types

import org.scalatest._

class PeakDataTest extends WordSpec with Matchers {
  "`PeakData` types" should {
    "correctly compute for relative intensities given absolute intensities only" in {
      PeakData(List(
        AbsolutePeakPair(646.3223, 64380108),
        AbsolutePeakPair(647.3252, 26819201),
        AbsolutePeakPair(648.3309, 7305831)
      )).peaks shouldBe
        List(
          CompletePeakTriple(646.3223, 64380108, 999),
          CompletePeakTriple(647.3252, 26819201, 416),
          CompletePeakTriple(648.3309, 7305831, 113)
        )

      PeakData(List(
        AbsolutePeakPair(78.9612, 22.14),
        AbsolutePeakPair(96.9706, 68.24),
        AbsolutePeakPair(242.0826, 6.547),
        AbsolutePeakPair(454.0890, 10.7),
        AbsolutePeakPair(455.1043, 16.62)
      )).peaks shouldBe
        List(
          CompletePeakTriple(78.9612, 22.14, 324),
          CompletePeakTriple(96.9706, 68.24, 999),
          CompletePeakTriple(242.0826, 6.547, 96),
          CompletePeakTriple(454.0890, 10.7, 157),
          CompletePeakTriple(455.1043, 16.62, 243)
        )
    }
  }

  "give a complete peak triple given relative intensities only by setting absolute intensity as relative" in {
    PeakData(List(
      RelativePeakPair(646.3223, 999),
      RelativePeakPair(647.3252, 416),
      RelativePeakPair(648.3309, 113)
    )).peaks shouldBe
      List(
        CompletePeakTriple(646.3223, 999, 999),
        CompletePeakTriple(647.3252, 416, 416),
        CompletePeakTriple(648.3309, 113, 113)
      )
  }

  "leave complete peak triples untouched" in {
    PeakData(List(
      CompletePeakTriple(78.9612, 22.14, 324),
      CompletePeakTriple(96.9706, 68.24, 999),
      CompletePeakTriple(242.0826, 6.547, 96),
      CompletePeakTriple(454.0890, 10.7, 157),
      CompletePeakTriple(455.1043, 16.62, 243)
    )).peaks shouldBe
      List(
        CompletePeakTriple(78.9612, 22.14, 324),
        CompletePeakTriple(96.9706, 68.24, 999),
        CompletePeakTriple(242.0826, 6.547, 96),
        CompletePeakTriple(454.0890, 10.7, 157),
        CompletePeakTriple(455.1043, 16.62, 243)
      )
  }
}
