package jp.riken.massbank.parser.scala.types

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
case class Complete(mz: Int, int: Double, rel: Double) extends Peak
case class AbsoluteOnly(mz: Int, int: Double) extends Peak
case class RelativeOnly(mz: Int, rel: Double) extends Peak

case class PeakData(peaks: List[Peak]) {
  val hasRelativeIntensity: Boolean = false
  val hasAbsoluteIntensity: Boolean = false
}
