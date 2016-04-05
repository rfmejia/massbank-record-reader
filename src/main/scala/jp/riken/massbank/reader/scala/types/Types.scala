package jp.riken.massbank.reader.scala.types

sealed trait Required
trait Mandatory extends Required
trait Optional extends Required

sealed trait UI
trait Unique extends UI
trait Iterative extends UI
trait Both extends Unique with Iterative

sealed trait LineType
trait SingleLine extends LineType
trait MultipleLine extends LineType

trait Peak
case class CompletePeakTriple(mz: Double, int: Double, rel: Double) extends Peak
case class AbsolutePeakPair(mz: Double, int: Double) extends Peak
case class RelativePeakPair(mz: Double, rel: Double) extends Peak

case class PeakData(peaks: List[Peak]) {
  val hasRelativeIntensity: Boolean = false
  val hasAbsoluteIntensity: Boolean = false
}
