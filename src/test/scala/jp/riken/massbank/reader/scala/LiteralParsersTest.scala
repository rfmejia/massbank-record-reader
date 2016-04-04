package jp.riken.massbank.reader.scala

import org.scalatest._
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import scala.util.Try

class LiteralParsersTest extends WordSpec with GeneratorDrivenPropertyChecks with Matchers with LiteralParsers {

  def checkUnequalLength[T](length: Int, result: T) =
    if (result.toString.length === length) {
      fail(s"Parsing of an illegal value was successful: expected length $length, got ${result.toString.length} for input $result")
    }

  "`anyString` literal parser" must {
    "parse any non-empty string" in {
      forAll(Generators.validString) { s => parse(anyString, s) shouldBe a[Success[_]] }
      forAll(Generators.validInt) { i => parse(integer, i.toString) shouldBe a[Success[_]] }
      forAll(Generators.validDouble) { d => parse(double, d.toString) shouldBe a[Success[_]] }
    }

    "reject non-empty strings" in { parse(anyString, "") shouldBe a[Failure] }
  }

  "`integer` literal parser" must {
    "parse valid integers" in {
      forAll(Generators.validInt) { i => parse(integer, i.toString) shouldBe a[Success[_]] }
    }

    "reject strings and doubles" in {
      val invalidInt = Generators.validString suchThat (s => Try(s.toInt).isFailure)
      forAll(invalidInt) { s: String =>
        parse(integer, s) map { result => checkUnequalLength(s.length, result) }
      }
      forAll(Generators.validDouble) { d =>
        val s = d.toString
        parse(integer, s) map { result => checkUnequalLength(s.length, result) }
      }
    }
  }

  "`double` literal parser" must {
    "parse valid doubles" in {
      forAll(Generators.validDouble) { d => parse(double, d.toString) shouldBe a[Success[_]] }
    }

    "reject strings and integers" in {
      val invalidDouble = Generators.validString.suchThat(s => Try(s.toDouble).isFailure)
      forAll(invalidDouble) { s =>
        parse(double, s) map { result => checkUnequalLength(s.length, result) }
      }
      forAll(Generators.validInt) { i =>
        val s = i.toString
        parse(double, s) map { result => checkUnequalLength(s.length, result) }
      }
    }
  }

  "`date` literal parser" must {
    "parse valid dates" in {
      forAll(Generators.validDate) { s => parse(date, s) shouldBe a[Success[_]] }
    }
  }

  "`dbLink` literal parser" should {
    "parse valid database link pairs" in {
      forAll(Generators.validLink) { s =>
        parse(dbLink, s) shouldBe a[Success[_]]
      }
    }
  }
}
