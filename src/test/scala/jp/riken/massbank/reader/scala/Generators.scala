package jp.riken.massbank.reader.scala

import org.scalacheck.{ Gen, Shrink }

object Generators {

  def noShrink[T] = Shrink[T](_ => Stream.empty)

  val length = 50

  def chars: Gen[Char] = Gen.oneOf(Gen.alphaNumChar, Gen.oneOf[Char](" -!\"#$%&'()-^\\@[;:,./=~|`{+*<>?_"))

  val validString = for (cs <- Gen.listOfN(length, chars)) yield cs.mkString

  val validInt = Gen.chooseNum(Int.MinValue, Int.MaxValue)

  val validDouble = Gen.chooseNum(Double.MinValue, Double.MaxValue)

  val validDate = for {
    year <- Gen.chooseNum(1900, 9999)
    month <- Gen.chooseNum(1, 12)
    day <- {
      if (month == 2) Gen.chooseNum(1, 28)
      else if (List(4, 6, 9, 11).contains((month))) Gen.chooseNum(1, 30)
      else Gen.chooseNum(1, 31)
    }
  } yield s"$year.$month.$day"

  val validLink = for {
    db <- Gen.oneOf("CAS", "INCHIKEY", "KEGG", "PUBCHEM", "NCBI-TAXONOMY")
    link <- validInt
  } yield s"$db $link"

  val completePeak = for {
    mz <- validInt
    int <- validDouble
    relInt <- validDouble
  } yield s"\t$mz $int $relInt"

  val partialPeak = for {
    mz <- validInt
    int <- validDouble
  } yield s"\t$mz $int"
}
